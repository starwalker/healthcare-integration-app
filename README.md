# Proof Kit: Spring Boot + Kafka (AWS MSK) + EKS + Terraform + Helm

This repository provides a minimal, production-leaning proof kit to demonstrate:
- A Spring Boot microservice exposing a REST endpoint and producing events to Kafka
- A Kafka consumer that processes events and supports replay and dead-letter
- Helm chart for Kubernetes deployment (EKS-ready)
- Terraform stubs for MSK (Kafka) and IAM scaffolding
- Notes for SLOs, retries, DLQs, and HIPAA-aligned auditability

> Replace placeholder values (VPC, subnets, security groups, image repo, domains). The Terraform is intentionally conservative and may require adjustments to your AWS account conventions.
