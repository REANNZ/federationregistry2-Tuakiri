package aaf.fr.app

class ErrorController {

  def notFound() {
    render view: "/404"
  }

  def notPermitted() {
    render view: "/403"
  }

}
