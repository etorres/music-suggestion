# Music Suggestion

[![Build Status](https://circleci.com/gh/etorres/music-suggestion.svg?style=svg)](https://circleci.com/gh/etorres/music-suggestion)

This is an (ongoing) implementation of the project 26 of the list of [40 Side Project Ideas for Software Engineers](https://www.codementor.io/@npostolovski/40-side-project-ideas-for-software-engineers-g8xckyxef):

> Music suggestion tool (suggested implementation: build a wrapper for the Spotify API). Create a tool that tracks the music you listen to and generates a playlist with similar qualities, but of songs you haven’t heard before. The Spotify API provides all of the tools needed to extract what you’ve listened to and create a playlist — the recommendation engine is up to you!

## Contributing to the project

Make sure you have [serverless](https://www.serverless.com/framework/docs/providers/aws/guide/installation/) installed and configured in your laptop. You can find a short _serverless_ installation guide at the end of this file.

### Building and running tests of this project

```shell script
jenv exec sbt clean test
```

### Deploying a new version of this project to AWS

First, you should package the service with the following command:

```shell script
jenv exec sbt assembly
```

Finally, you can deploy the new version to AWS:

```shell script
sls deploy -v
```

### Testing this project in AWS

You can invoke this application locally:

```shell script
sls invoke --local --function hello
```

Or you can trigger an invocation by accessing the API gateway like this:

```shell script
curl -v https://{API_Gateway_Id}.execute-api.{AWS_Zone}.amazonaws.com/dev/greet/guest
```

## Cleaning-up your service from AWS

Executing the following command from your project's root directory will remove all the infrastructure on this service from AWS:

```shell script
sls remove
```

## Examining this project

Code coverage reports can be created by running the following command:

```shell script
jenv exec sbt clean coverage test ; jenv exec sbt coverageReport
```

Similarly, a dependency graph can be created with the following command (it will display in a browser):

```shell script
jenv exec sbt dependencyBrowseGraph
```

## Installing serverless

Make sure you read the official [serverless](https://www.serverless.com/framework/docs/providers/aws/guide/installation/) installation guide before proceeding with the installation.

Install the _serverless framework_:

```shell script
npm install -g serverless
```

(Only for new `aws-cli` installations) configure your AWS account with:
```shell script
aws configure
```

You can create an example project by running the following command in an empty directory:

```shell script
sls create --template aws-scala-sbt
```

## Troubleshooting

Test AWS credentials for command line tools:

```shell script
aws sts get-caller-identity --profile serverless-music-suggestion
```

## TO-DO

Add the following plugin to parse the CloudFormation output and extract the service endpoint:

[Serverless plugin to process AWS CloudFormation Stack Output](https://github.com/sbstjn/serverless-stack-output).

Use curl to test the service endpoint in CI.

## Additional Resources

* [Serverless Framework Documentation](https://www.serverless.com/framework/docs/).
* [How to Setup AWS Lambda in Scala without any external Library](https://edward-huang.com/aws/cloud/2019/11/28/how-to-setup-aws-lambda-in-scala-without-any-external-library/).
* [Get the User's Currently Playing Track](https://developer.spotify.com/documentation/web-api/reference-beta/#endpoint-get-the-users-currently-playing-track).
* [AWS Lambda function handler in Java](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html).
* [AWS Lambda: Cold boot and mean response times in Scala vs. Java](https://blog.codecentric.de/en/2019/02/aws-lambda-cold-boot-and-mean-response-times-in-scala-vs-java/).
* [Benefits of Amazon API Gateway](https://www.serverless.com/amazon-api-gateway/).