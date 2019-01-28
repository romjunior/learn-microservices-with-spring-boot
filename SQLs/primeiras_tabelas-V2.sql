create table usuario (
	id bigserial not null,
	alias varchar(500) not null,
	CONSTRAINT pk_usuario PRIMARY KEY (id)
);

create table multiplication (
	id bigserial not null,
	factora int not null,
	factorb int not null,
	CONSTRAINT pk_multiplication PRIMARY KEY(id)
);

create table multiplication_result_attempt (
	id bigserial not null,
	correct boolean not null,
	resultattempt int not null,
	multiplication_id bigint not null,
	usuario_id bigint not null,
	CONSTRAINT pk_multiplication_result_attempt PRIMARY KEY(id),
	CONSTRAINT fk_multiplication_result_attempt_usuario FOREIGN KEY(usuario_id)
						REFERENCES usuario(id),
	CONSTRAINT fk_multiplication_result_attempt_multiplication FOREIGN KEY(multiplication_id)
						REFERENCES multiplication(id)
	
);