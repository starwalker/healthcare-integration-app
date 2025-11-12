variable "aws_region" {
  type        = string
  description = "AWS region"
  default     = "us-east-1"
}

variable "vpc_id" {
  type = string
}

variable "subnet_ids" {
  type = list(string)
}

variable "security_group_ids" {
  type = list(string)
}

variable "msk_cluster_name" {
  type    = string
  default = "proof-msk-cluster"
}

variable "eks_oidc_provider" {
  type        = string
  description = "EKS OIDC provider host/path without https://"
}
