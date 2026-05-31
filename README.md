# Rick & Morty Hub 🛸

Um aplicativo Android moderno e elegante que consome a [Rick and Morty API](https://rickandmortyapi.com/) para explorar todo o multiverso da série.

## 📱 Funcionalidades
- **Listagem de Personagens**: Visualize todos os personagens da série em uma lista fluida com rolagem infinita (paginação automática).
- **Busca por Nome**: Encontre rapidamente o seu personagem favorito digitando o nome.
- **Filtros Interativos**: Filtre os personagens por Status (Vivo, Morto, Desconhecido) e Gênero (Masculino, Feminino).
- **Detalhes Completos**: Acesse o perfil completo do personagem com uma interface moderna estilo card.
- **Favoritos Offline**: Salve seus personagens favoritos. O app utiliza banco de dados local (`Room`) para garantir que os personagens salvos permaneçam lá mesmo sem internet.

## 🛠️ Tecnologias Utilizadas
- **Kotlin**: Linguagem base do projeto.
- **MVVM (Model-View-ViewModel)**: Arquitetura robusta para separar as regras de negócio da UI.
- **Retrofit**: Para consumo da API REST e comunicação com os servidores.
- **Coil**: Para carregamento assíncrono e cache das imagens.
- **Room Database**: Persistência de dados locais para os favoritos.
- **Coroutines & Flow**: Programação assíncrona moderna e reativa.
- **ViewBinding**: Interação segura com a interface de usuário (XML).

## 🚀 Como Executar o Projeto

1. Clone o repositório para a sua máquina:
```bash
git clone <url-do-repositorio>
```
2. Abra o projeto no **Android Studio**.
3. Aguarde a sincronização do **Gradle**.
4. Clique em **Run** (`Shift + F10`) e emule em um dispositivo virtual ou físico com Android.

## 📸 Imagens do Projeto
A interface foi recentemente reformulada com base em designs modernos, contando com Splash Screen, imagens circulares arredondadas e badges indicadores de status de vida.

---
Desenvolvido com dedicação 💚
