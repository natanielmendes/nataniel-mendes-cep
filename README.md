# nataniel-mendes-cep
Aplicação Java EE para cadastro de endereços e consulta por CEP

A aplicação possui os seguintes componentes:

SharedLibrary: Esta é uma biblioteca compartilhada responsável pelo gerenciamento de entidades no projeto. No momento, temos somente uma entidade, Endereco, que realiza persistência na tabela tb_end.

ProjetoCedroBKS: Aplicação WEB, utilizada no momento para definir o contexto/caminho das APIs RESTful, mais adiante será utilizada para a parte WEB, no momento não foi implementada qualquer interface web front-end.

ModuloEJBCedroBKS: Componente responsável pelos beans de sessão utilizados pelos WebServices presentes no mesmo componente, também responsável pela conversão das requisições get e post para objetos e aplicação das regras de negócio.

AppEnterpriseCedroBKS: Faz a junão dos módulos para gerar o EAR, que será posteriormente aplicado através de deploy no servidor Wildfly.

AppRESTClient: Aplicação Java simples, para fazer a requisição POST para cadastro de novo endereço.

O banco de dados escolhido foi o Derby, que está sendo acessado pela aplicação através da API JDBC. Segue abaixo o script de criação da tabela tb_end e sua sequence para auto-incremento de id.

CREATE TABLE "ME"."TB_END"
(
   END_ID INTEGER NOT NULL,
   CEP varchar(8),
   ENDERECO varchar(40),
   BAIRRO varchar(30),
   CIDADE varchar(30),
   ESTADO varchar(2),
   PRIMARY KEY (END_ID)
);

CREATE SEQUENCE end_id_sequence AS INTEGER START WITH 1 INCREMENT BY 1 NO MAXVALUE;

FUNCIONALIDADES:

No momento, a aplicação possui 2 funcionalidades, a de busca através de CEP e a de cadastro de novo endereço, ambas consomem e geram JSON:

A busca através do CEP só é feita quando o mesmo é válido, ou seja, possui 8 dígitos numéricos, podendo ser escrito nos formatos "00000000" ou "00000-000". Segue abaixo modelo de requisição através do browser:

http://localhost:8080/ProjetoCedroBKS/cep/rest-api/busca/12345678
* É importante lembrar que localhost:8080 é uma URL padrão, muito provavelmente este serviço estará hospedado em um servidor público com URL diferente.

O cadastro poderá ser relizado através da aplicação AppRESTClient, que já possui, inclusive, um JSON de exemplo, que também pode ser visto abaixo:

{
	"endereco": {
		"cep": "12345678",
		"endereco": "Rua da Sequencia, 123",
		"bairro": "Bairro da Numeracao",
		"cidade": "Sao Paulo",
		"estado": "SP"
	}
}







