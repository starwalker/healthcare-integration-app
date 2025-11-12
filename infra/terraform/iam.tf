data "aws_caller_identity" "current" {}

data "aws_iam_policy_document" "assume_role" {
  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRoleWithWebIdentity"]
    principals {
      type        = "Federated"
      identifiers = ["arn:aws:iam::${data.aws_caller_identity.current.account_id}:oidc-provider/${var.eks_oidc_provider}"]
    }
    condition {
      test     = "StringEquals"
      variable = "${var.eks_oidc_provider}:sub"
      values   = ["system:serviceaccount:health:health-svc"]
    }
  }
}

resource "aws_iam_role" "msk_role" {
  name               = "health-msk-producer-consumer"
  assume_role_policy = data.aws_iam_policy_document.assume_role.json
}

data "aws_iam_policy_document" "msk_access" {
  statement {
    effect = "Allow"
    actions = [
      "kafka-cluster:Connect",
      "kafka-cluster:DescribeCluster",
      "kafka-cluster:AlterCluster",
      "kafka-cluster:DescribeGroup",
      "kafka-cluster:AlterGroup",
      "kafka:DescribeTopic",
      "kafka:ReadData",
      "kafka:WriteData"
    ]
    resources = ["*"]
  }
}

resource "aws_iam_policy" "msk_policy" {
  name   = "health-msk-access"
  policy = data.aws_iam_policy_document.msk_access.json
}

resource "aws_iam_role_policy_attachment" "attach" {
  role       = aws_iam_role.msk_role.name
  policy_arn = aws_iam_policy.msk_policy.arn
}
