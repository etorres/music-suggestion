package es.eriktorr.music.recommendation

import es.eriktorr.music.aws.lambda.proxy.ApiGatewayRequestHandler

trait MusicRecommenderHandler extends ApiGatewayRequestHandler[MusicFeatures, MusicRecommendation]
