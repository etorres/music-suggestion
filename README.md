# Photo Diary

```shell script
npm install -g serverless

npm install middy

npm install aws-sdk
```

(Only for new `aws-cli` installations)
```shell script
aws configure
```

```shell script
sls create --template aws-scala-sbt
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

## Troubleshooting

Test AWS credentials for command line tools:

```shell script
aws sts get-caller-identity --profile serverless-music-suggestion
```

## Additional Resources

* [Serverless Framework Documentation](https://www.serverless.com/framework/docs/).
