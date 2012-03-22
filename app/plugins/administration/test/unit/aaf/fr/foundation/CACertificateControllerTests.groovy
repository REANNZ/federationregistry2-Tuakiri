package aaf.fr.foundation



import org.junit.*
import grails.test.mixin.*

@TestFor(CACertificateController)
@Mock(CACertificate)
class CACertificateControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/CACertificate/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.CACertificateInstanceList.size() == 0
        assert model.CACertificateInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.CACertificateInstance != null
    }

    void testSave() {
        controller.save()

        assert model.CACertificateInstance != null
        assert view == '/CACertificate/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/CACertificate/show/1'
        assert controller.flash.message != null
        assert CACertificate.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/CACertificate/list'


        populateValidParams(params)
        def CACertificate = new CACertificate(params)

        assert CACertificate.save() != null

        params.id = CACertificate.id

        def model = controller.show()

        assert model.CACertificateInstance == CACertificate
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/CACertificate/list'


        populateValidParams(params)
        def CACertificate = new CACertificate(params)

        assert CACertificate.save() != null

        params.id = CACertificate.id

        def model = controller.edit()

        assert model.CACertificateInstance == CACertificate
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/CACertificate/list'

        response.reset()


        populateValidParams(params)
        def CACertificate = new CACertificate(params)

        assert CACertificate.save() != null

        // test invalid parameters in update
        params.id = CACertificate.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/CACertificate/edit"
        assert model.CACertificateInstance != null

        CACertificate.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/CACertificate/show/$CACertificate.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        CACertificate.clearErrors()

        populateValidParams(params)
        params.id = CACertificate.id
        params.version = -1
        controller.update()

        assert view == "/CACertificate/edit"
        assert model.CACertificateInstance != null
        assert model.CACertificateInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/CACertificate/list'

        response.reset()

        populateValidParams(params)
        def CACertificate = new CACertificate(params)

        assert CACertificate.save() != null
        assert CACertificate.count() == 1

        params.id = CACertificate.id

        controller.delete()

        assert CACertificate.count() == 0
        assert CACertificate.get(CACertificate.id) == null
        assert response.redirectedUrl == '/CACertificate/list'
    }
}
