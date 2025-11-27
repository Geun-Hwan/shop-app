# Dockerfile
FROM docker.elastic.co/elasticsearch/elasticsearch:8.18.8

# Nori 플러그인 설치 (한 번만)
RUN elasticsearch-plugin install --batch analysis-nori

# 컨테이너 시작 시 Elasticsearch 실행
CMD ["bin/elasticsearch"]