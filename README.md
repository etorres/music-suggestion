# Music Suggestion

This is an implementation of the project 26 of the list of [40 Side Project Ideas for Software Engineers](https://www.codementor.io/@npostolovski/40-side-project-ideas-for-software-engineers-g8xckyxef):

> Music suggestion tool (suggested implementation: build a wrapper for the Spotify API). Create a tool that tracks the music you listen to and generates a playlist with similar qualities, but of songs you haven’t heard before. The Spotify API provides all of the tools needed to extract what you’ve listened to and create a playlist — the recommendation engine is up to you!

## Contributing to the project

Install the _serverless framework_:

```shell script
npm install -g serverless
```

(Only for new `aws-cli` installations)
```shell script
aws configure
```

```shell script
sls create --template aws-scala-sbt
```

```shell script
jenv exec sbt clean test
```

```shell script
jenv exec sbt assembly
```

```shell script
sls deploy -v
```

```shell script
sls invoke --local --function hello
```

## Clean-up

```shell script
sls remove
```

TODO:

```shell script
jenv exec sbt clean coverage test ; jenv exec sbt coverageReport
```

## Troubleshooting

Test AWS credentials for command line tools:

```shell script
aws sts get-caller-identity --profile serverless-music-suggestion
```

## Additional Resources

* [Serverless Framework Documentation](https://www.serverless.com/framework/docs/).
* [How to Setup AWS Lambda in Scala without any external Library](https://edward-huang.com/aws/cloud/2019/11/28/how-to-setup-aws-lambda-in-scala-without-any-external-library/).
