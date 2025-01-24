# API-in-Java
# TrabalhoLP2-unidade03

## Docker: 
docker build -t image-test-spring .

docker run -p 8080:8080 --name spring-app image-test-spring

## Links para baixar YOLO:
### Não foi possível deixar os arquivos dentro do repositório por conta do tamanho. Portanto, devem ser baixados localmente.

https://raw.githubusercontent.com/pjreddie/darknet/master/cfg/yolov3.cfg

https://pjreddie.com/media/files/yolov3.weights

https://raw.githubusercontent.com/pjreddie/darknet/master/data/coco.names


## Arquivo .env:
É necessário cirar um arquivo .env na raiz do projeto com as seguintes variáveis:

Substitua "/path/to/" para o caminho dos arquivos instalados no seu computador.

MODEL_CONFIGURATION=/path/to/yolov3.cfg

MODEL_WEIGHTS=/path/to/yolov3.weights

CLASS_NAMES_FILE=/path/to/coco.names

