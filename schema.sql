DROP DATABASE mc536;
CREATE DATABASE mc536;
USE mc536;

CREATE TABLE Pessoa(
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
login VARCHAR(30) NOT NULL,
uri varchar(100) NOT NULL,
nome varchar(100) NOT NULL,
cidade_natal VARCHAR(60),
ativa BIT DEFAULT 1
);


CREATE TABLE Conhece(
id_pessoa INT NOT NULL,
id_conhecido INT NOT NULL,
FOREIGN KEY (id_pessoa)
        REFERENCES Pessoa(id),
FOREIGN KEY (id_conhecido)
        REFERENCES Pessoa(id)
);


CREATE TABLE Bloqueio(
id_pessoa INT NOT NULL,
id_bloqueado INT NOT NULL,
tipoBloqueio VARCHAR(255) NOT NULL,
descricao VARCHAR(511),
FOREIGN KEY (id_pessoa)
        REFERENCES Pessoa(id),
FOREIGN KEY (id_bloqueado)
        REFERENCES Pessoa(id)
);


CREATE TABLE ArtistaMusical(
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
nome_artistico VARCHAR(60) NOT NULL,
pais VARCHAR(60) NOT NULL,
genero VARCHAR(30) NOT NULL,
url VARCHAR(255) NOT NULL
);


CREATE TABLE PessoaCurteArtistaMusical(
id_pessoa INT NOT NULL,
id_artista_musical INT NOT NULL,
nota INT,
data_curtida DATE NOT NULL,
FOREIGN KEY (id_pessoa)
        REFERENCES Pessoa(id),
FOREIGN KEY (id_artista_musical)
        REFERENCES ArtistaMusical(id)
);


CREATE TABLE Musico(
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
nome_real VARCHAR(60) NOT NULL,
estilo_musical VARCHAR(60) NOT NULL,
data_nascimento DATE
);


CREATE TABLE Banda(
        id INT NOT NULL PRIMARY KEY,
        data_fim DATE,
        data_surgimento DATE NOT NULL,
        FOREIGN KEY (id)
REFERENCES ArtistaMusical(id)
);


CREATE TABLE MusicoBanda(
        id_musico INT NOT NULL,
        id_banda INT NOT NULL,
        data_entrada date not null,
        data_saida date,
FOREIGN KEY (id_musico)
REFERENCES Musico(id),
        FOREIGN KEY (id_banda)
REFERENCES Banda(id)
);


CREATE TABLE Cantor(
id INT NOT NULL,
id_musico INT NOT NULL,
FOREIGN KEY (id)
        REFERENCES ArtistaMusical(id),
FOREIGN KEY (id_musico)
        REFERENCES Musico(id)
);


CREATE TABLE ArtistaMuicalInspirou(
        id_artista INT NOT NULL,
        id_inspirou INT NOT NULL,
FOREIGN KEY (id_artista)
REFERENCES ArtistaMusical(id),
FOREIGN KEY (id_inspirou)
REFERENCES ArtistaMusical(id)
);


CREATE TABLE ArtistaMusicalTocouEm(
        id_artista INT NOT NULL,
        id_artista2 INT NOT NULL,
        FOREIGN KEY (id_artista)
REFERENCES ArtistaMusical(id),
        FOREIGN KEY (id_artista2)
REFERENCES ArtistaMusical(id)
);