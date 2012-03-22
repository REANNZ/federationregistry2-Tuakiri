package aaf.fr.foundation



import org.junit.*
import grails.test.mixin.*

@TestFor(SamlURIController)
@Mock(SamlURI)
class SamlURIControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/samlURI/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.samlURIInstanceList.size() == 0
        assert model.samlURIInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.samlURIInstance != null
    }

    void testSave() {
        controller.save()

        assert model.samlURIInstance != null
        assert view == '/samlURI/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/samlURI/show/1'
        assert controller.flash.message != null
        assert SamlURI.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/samlURI/list'


        populateValidParams(params)
        def samlURI = new SamlURI(params)

        assert samlURI.save() != null

        params.id = samlURI.id

        def model = controller.show()

        assert model.samlURIInstance == samlURI
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/samlURI/list'


        populateValidParams(params)
        def samlURI = new SamlURI(params)

        assert samlURI.save() != null

        params.id = samlURI.id

        def model = controller.edit()

        assert model.samlURIInstance == samlURI
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/samlURI/list'

        response.reset()


        populateValidParams(params)
        def samlURI = new SamlURI(params)

        assert samlURI.save() != null

        // test invalid parameters in update
        params.id = samlURI.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/samlURI/edit"
        assert model.samlURIInstance != null

        samlURI.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/samlURI/show/$samlURI.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        samlURI.clearErrors()

        populateValidParams(params)
        params.id = samlURI.id
        params.version = -1
        controller.update()

        assert view == "/samlURI/edit"
        assert model.samlURIInstance != null
        assert model.samlURIInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/samlURI/list'

        response.reset()

        populateValidParams(params)
        def samlURI = new SamlURI(params)

        assert samlURI.save() != null
        assert SamlURI.count() == 1

        params.id = samlURI.id

        controller.delete()

        assert SamlURI.count() == 0
        assert SamlURI.get(samlURI.id) == null
        assert response.redirectedUrl == '/samlURI/list'
    }
}
