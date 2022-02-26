# ![](logo.png) KBlog

A simple blog software using kotlin ktor with markdown and templating. It uses only `.md` files and static files for
content.

## How to build

You need to have a working `1.8+` jre environment. In root directory :

```bash
./gradlew build
# or in windows
.\gradlew.bat build
```

Build release package is inside `./kblog/build/distributions`

## How to use

First download kblog software. Either by [the release page](https://github.com/L-Briand/kblog/releases) or by building
it yourself via the section above. You also need to have a working `1.8+` jre environment. (`java -version` should yield
something.)

Extract the package :

```bash
# with tar 
tar -xf ./kblog/build/distributions/kblog-0.0.1.tar

# with zip
unzip ./kblog/build/distributions/kblog-0.0.1.zip
```

Then run it : `./kblog-0.0.1/bin/kblog`. (A `kblog.bat` exists for windows.)

## How does it works

### Content folder

By default, it uses the sub-folder `blog` at where the program is launched as a root directory for content delivery. If
you have cloned this repository you should have a `blog` folder at root with minimal content just for
show. [You can take a look by yourself.](./blog)

Inside the `blog` folder lies two sub-folder :

- `templates` It holds templates for **markdown** articles to be rendered as html.
  *For now, only templates/article.html is used since it renders only markdown articles files.*
- `public` It contains all publicly available content and articles. Available through `http://localhost:8080/public/`

### Articles render

All files ending with `.art.md` inside the `public` folder are considered "articles" and are directly accessible page on
the website. Example : `public/my_article.art.md` will show as `http://localhost:8080/my_article`.

Default index file is `index.art.md` and if an article isn't found it defaults to `404.art.md`.

If file contains diacritics, the name is automatically freed from them. Example : `./public/Suis-je étonné ?.art.md`
will appear as `http://localhost:8080/Suis_je_etonne__`. You can have some undefined behavior if two articles collapse
to the same name, but it should not happen too frequently.

With default template, page title is the first H1 of markdown file.

### Arguments

| Argument        | default | Description                    | example                   |
|-----------------|---------|--------------------------------|---------------------------|
| -d, --directory | blog    | Set content delivery folder    | `kblog -d my/custom/path` |
| -e, --extension | .art.md | Set file extension for article | `kblog -e .md`            |
| -p, --port      | 8080    | Set port                       | `kblog -p 80`             |
| -l, --listen    | 0.0.0.0 | Defaults listen connections    | `kblog -l 127.0.0.1`      |
