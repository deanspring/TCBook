TCBook
======

TCBook: rede social contendo informações sobre gostos musicais dos usuários.

Dados da Base:
tcbook.db.url=jdbc:mysql://localhost:3306/mc536
tcbook.db.username=tcbook-ws
tcbook.db.password=tcbook123

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
	  - job
	    - Contém a implementação dos jobs de data extraction e data cleaning
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


--> Tarefa de estatísticas:
    - Backend:
        - As classes importantes são:
            - src/main/com/tcbook/ws/web/rest/StatisticsRS.java : Contém a interface que expõe as estatísticas para o front-end.
            - src/main/com/tcbook/ws/core/bo/StatisticsBO.java : Contém toda a lógica de tratamento dos dados vindos da base de dados.
            - Consultas SQL para extração de estatísticas podem ser encontradas em:
                - src/main/java/com/tcbook/ws/database/dao/ColleagueDAOImpl.java: Método topTenKnownWithMostSharedArtists()
                - src/main/java/com/tcbook/ws/database/dao/MusicalArtistGenresDAOImpl.java: Método topFivePopularGenres()
                - src/main/java/com/tcbook/ws/database/dao/PersonDAOImpl.java: Método topTenEclecticPeople()
                - src/main/java/com/tcbook/ws/database/dao/PersonLikeMusicalArtistDAOImpl.java: Métodos generalRatings(), averageByArtist(), topTwentyAveragesByArtist(), topTenPopularArtists(), topTenStandardDeviationByArtist(), artistsPopularity(), peopleLikesByAmount() e artistsLikesByAmount()
                - src/main/java/com/tcbook/ws/database/dao/RegionDAOImpl.java: Método topTenCountriesWithMostArtists()