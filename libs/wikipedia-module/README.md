# Winchester 2 - wikipedia module

Instrucciones de uso:
  Utilizar el injector WikipediaInjector.kt para acceder al valor de wikipediaService el cual contiene el servicio externo; dicho servicio retorna objetos del tipo Description. El objeto mencionado anteriormente retorna la url de la fuente, una descripción y la url del logo de wikipedia.

 Para obtener la referencia:
  val wikipediaService = ayds.winchester2.wikipedia.WikipediaInjector.wikipediaService
  
 Para llamar al servicio:
  val descriptionWikipedia = wikipediaService.getArtistDescription(name)
  
  Edge Cases:
  Sin conexion a internet: wikipediaArticle es nulo 
  
  No hay resultado: wikipediaArticle es nulo 
