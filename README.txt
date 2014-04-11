TCBook
======

TCBook: rede social contendo informações sobre gostos musicais dos usuários.

Pastas:
TCBook/src/main/webapps: diretório contendo arquivos necessários para o front-end.
TCBook/src/main/java/: diretório contendo o código fonte do back-end (Java)

- Backend:
	Pacote raíz: com.tcbook.ws
	Subpacotes:
	  - bean: Contém entidades utilizadas pelo sistema: MusicalArtist (ArtistaMusical) e Person (Pessoa). São apenas beans, sem lógica alguma.
	  - bin: Contém classes de inicialização e desligamento do sistema, são responsáveis por iniciar e desligar recursos comuns ao sistema, respectivamente.
	  - core.bo: Contém a classe PersonBO, responsável pelas regras de negócio relativas às operações de pessoa. Atualmente existe para garantir consistência da base. Futuramente conterá controles de acesso, comunicações com cache, etc.
	  - database.dao:
	  	- DAO: interface de operações comuns aos DAOs
	  	- ColleagueDAO / ColleagueDAOImpl: Interações com banco de dados relativo à tabela "Conhece"
	  	- MusicalArtistDAO / MusicalArtistDAOImpl: Interações com banco de dados relativo à tabela "ArtistaMusical"
	  	- PersonDAO / PersonDAOImpl: Interações com banco de dados relativo à tabela "Pessoa"
	  	- PersonLikeMusicalArtistDAO / PersonLikeMusicalArtistDAOImpl: Interações com banco de dados relativo à tabela "PessoaCurteArtistaMusical"
	  - database.datasource:
	  	- Contém classes relativas à conexão com banco de dados.
	  - util:
	  	- Classes utiitárias: gerenciamento de configuração e constantes comuns ao sistema
	  - web.manager:
	  	- Classe responsável por inicializar o servidor Jetty embutido
	  - web.rest:
	  	- Classes responsáveis por responder as requisições recebidas. Relativas à monitoramento, operações com artistas musicais e operações sobre pessoa

- Frontend:
	- Utilizamos os frameworks bootstrap: http://getbootstrap.com/ para questões de css e html.
	- Para que o "sistema" seja single page (carrega uma unica vez um .html) fizemos carregamentos assincronos (ajax) de templates doT. http://olado.github.io/doT/index.html
	- Para manipulação do dom, usamos jQuery: http://api.jquery.com/
	- Para comunicação client-side, server-side utilizamos o protocolo JSON http://www.json.org/