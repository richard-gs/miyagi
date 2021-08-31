package miyagi

// http://docs.grails.org/3.3.11/guide/theWebLayer.html#urlmappings
class UrlMappings {

    static mappings = {
        delete "/$controller/$id(.$format)?"(action:"delete")
        get    "/$controller(.$format)?"(action:"index")
        get    "/$controller/$id(.$format)?"(action:"show")
        post   "/$controller(.$format)?"(action:"save")
        put    "/$controller/$id(.$format)?"(action:"update")
        patch  "/$controller/$id(.$format)?"(action:"patch")

        get    "/$controller/search"(action:"search")
        get    "/$controller/elasticSearch"(action:"elasticSearch")

        "/"(view: '/index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
