# Testes da Atividade 2

## Verificações executadas durante a preparação

- Compilação dos fontes e dos testes: aprovada, usando compilador OpenJDK 21 com source/target 18. O compilador emitiu aviso de caminho de módulos não configurado para source 18; não houve erro de compilação.
- TesteValidacao: 11 cenários aprovados, incluindo nome vazio, valor vazio, texto, decimais, zero, negativo, overflow e produto válido.
- TesteDAO: aprovado com driver JDBC simulado. Verifica INSERT parametrizado, retorno de zero linhas, mapeamento do SELECT, consultas repetidas sem acúmulo, fechamento de recursos e propagação de falhas de conexão.

Esses testes não conectaram a um servidor MySQL e não executaram as janelas Swing. A integração real e as mensagens visuais devem ser testadas no computador do aluno antes da entrega. Nenhuma captura de execução foi fabricada.

## Executar os testes automatizados (JDK 18 ou superior)

Na raiz do projeto, usando Git Bash:

```bash
mkdir -p build/testes
javac -encoding UTF-8 -d build/testes src/*.java test/*.java
java -cp build/testes TesteValidacao
java -cp build/testes TesteDAO
```

O TesteDAO usa apenas o driver simulado: não adicione lib/* ao classpath desse teste.

## Testes manuais pendentes no ambiente de entrega

1. Executar o projeto no NetBeans: cadastroVIEW abre primeiro.
2. Cadastrar produto válido e confirmar a nova linha no MySQL.
3. Verificar mensagem de sucesso e limpeza dos campos.
4. Usar temporariamente uma porta de banco incorreta e verificar mensagem de falha, sem sucesso falso; restaurar a conexão.
5. Abrir a listagem e conferir todos os produtos com SELECT no Workbench.
6. Reabrir a listagem após novo cadastro: novo item visível, sem duplicação de linhas.
7. Tentar nome vazio, valor vazio, abc, 0, -1 e 10,50: mensagem de validação, sem inserção.
8. Fechar a listagem pelo X: a tela de cadastro permanece aberta.

O schema original utiliza INT para valor. Nesta atividade são aceitos valores inteiros positivos, sem centavos.

## Preparar conexão

Copie config-exemplo.properties para config.properties na raiz e preencha as credenciais locais. Execute o projeto com a raiz como diretório de trabalho (padrão do NetBeans). O arquivo local não deve ser versionado.

Se o banco da Atividade 1 já existe, use-o diretamente. Para uma instalação nova, importe banco/uc11.sql pelo Workbench. Não execute esse script novamente sobre uma tabela produtos já existente.
