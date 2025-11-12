# Architecture Blueprint (Concise)

## Overview
The service accepts HTTP requests, validates payloads, and publishes `PatientEvent` messages to Kafka (AWS MSK). A consumer processes the events, persists outcomes, and emits to a DLQ on irrecoverable failure. Deploy with Helm to EKS. Infrastructure is provisioned via Terraform: MSK cluster, networking attachments, and IAM roles for service accounts (IRSA).

## Data Flow
1. Client calls `POST /events/patient` with a JSON `PatientEvent` (idempotency-key optional).
2. Producer publishes to topic `${APP_TOPIC}` with key `${patientId}`.
3. Consumer subscribes to the same topic, processes events, and writes results.
4. On deserialization or business rule failure after retries, message is routed to `${APP_TOPIC}.DLQ` with headers indicating cause and original offset.

## Resiliency and Operations
- **At-least-once** consumption; idempotency keys to ensure downstream exactly-once semantics.
- **Backoff and retry** using Spring Retry; exponential backoff with jitter.
- **Replay** by seeking to offsets or re-publishing from compacted topic.
- **DLQ** for poison messages; triage job reads DLQ and produces remediation reports.
- **Observability**: structured logs with trace ids, basic metrics (throughput, lag, failure counts). Add Prometheus scrape with ServiceMonitor in clusters that run Prometheus Operator.
- **SLOs**: availability 99.9%, P50 publish latency <50 ms, P99 end-to-end <2 s for batch size <= 1. Error budget tracked via failed publishes/consumes per minute.

## Security and Compliance (HIPAA context)
- **Transport**: TLS for ingress and Kafka (MSK with TLS listeners).
- **AuthN**: IRSA for pod-level AWS access; MSK IAM/SASL where feasible.
- **AuthZ**: least-privilege IAM policy bound to the service account for produce/consume on specific topics only.
- **Auditability**: append-only event log in Kafka, immutable storage for audit (S3 lifecycle with object lock if required), structured logs with patient pseudonymization tokens.
- **PHI**: avoid PHI in logs. Use surrogate keys and encrypted payloads where needed.

## Failure Modes
- **Kafka unavailable**: circuit-break publishes, buffer small backlog, shed load when thresholds exceeded.
- **Schema evolution**: use schema registry or versioned DTOs; consumer tolerates additive fields.
- **Hot partitions**: key on a composite `(patientId|visitId|timestampBucket)` to spread load.
- **Stuck consumer**: lag alerts; autoscaling; safe shutdown with `max.poll.interval.ms` controls.

## Deployment
- Package image; push to GHCR or ECR.
- `helm upgrade --install health-integration-service deploy/helm/health-integration-service -n health --create-namespace`
- Provide env vars via values.yaml or external secrets (bootstrap servers, topic, consumer group).

## Cost and Scale
- Start with 3 brokers, 3 AZs, 3 partitions, replication factor 3.
- Scale partitions based on throughput. HorizontalPodAutoscaler for the consumer.
