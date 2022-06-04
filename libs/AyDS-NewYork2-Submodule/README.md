# Modulo NYTimes
Grupo NewYork2
Arqui. y Diseño 2022 @ UNS

## Qué hace

Busca en la API del diario norteamericano *New York Times* un artículo relacionado a un determinado artista.

Módulo para una aplicación de Android, programado en Kotlin.

## Instalación

1. Para añadir el submódulo al repositorio

Por HTTP:
```
git submodule add https://github.com/FacundoAlvarado9/AyDS-NewYork2-Submodule.git <directorio>
```

Por SSH:
```
git submodule add git@github.com:FacundoAlvarado9/AyDS-NewYork2-Submodule.git <directorio>
```

2. Indicar el path del módulo en *settings.gradle*
```gradle
include ':NewYorkTimesData'
project(':NewYorkTimesData').projectDir = new File(<directorio>)
```

3. Añadir la dependencia *build.gradle*
```gradle
implementation project(":NewYorkTimesData")
```

4. Sincronizar el proyecto con Gradle (sync)

## Uso

Importar el servicio

```kotlin
import ayds.newyork2.newyorkdata.nytimes.NYTimesService
```

Para obtener una instancia del servicio (en Kotlin)

```kotlin
val nytService : NYTimesService = NYTimesInjector.nyTimesService
```

Para hacer uso del módulo, utilizar el método getArtist

```kotlin
fun getArtist(artistName: String) : NYTimesArtistInfo?
```

El metodo se usa de la siguiente manera

```kotlin
nytService.getArtist("Duki")
```
Y esta llamada retorna: 

* O bien un objeto del tipo *NYTimesArtistInfo*, que tiene las propiedades

```kotlin
data class NYTimesArtistInfo(
    override val artistName: String,
    override val artistInfo: String,
    override val artistUrl: String,
) : ArtistInfo {
    val source_logo_url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
}
```
* O *null* en caso de no ser encontrado
* En el caso de que no se pueda resolver la consulta el metodo tira una Exception
