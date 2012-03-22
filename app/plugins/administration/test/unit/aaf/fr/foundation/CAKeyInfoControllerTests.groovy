package aaf.fr.foundation



import org.junit.*
import grails.test.mixin.*

@TestFor(CAKeyInfoController)
@Mock(CAKeyInfo)
class CAKeyInfoControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/CAKeyInfo/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.CAKeyInfoInstanceList.size() == 0
        assert model.CAKeyInfoInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.CAKeyInfoInstance != null
    }

    void testSave() {
        controller.save()

        assert model.CAKeyInfoInstance != null
        assert view == '/CAKeyInfo/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/CAKeyInfo/show/1'
        assert controller.flash.message != null
        assert CAKeyInfo.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/CAKeyInfo/list'


        populateValidParams(params)
        def CAKeyInfo = new CAKeyInfo(params)

        assert CAKeyInfo.save() != null

        params.id = CAKeyInfo.id

        def model = controller.show()

        assert model.CAKeyInfoInstance == CAKeyInfo
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/CAKeyInfo/list'


        populateValidParams(params)
        def CAKeyInfo = new CAKeyInfo(params)

        assert CAKeyInfo.save() != null

        params.id = CAKeyInfo.id

        def model = controller.edit()

        assert model.CAKeyInfoInstance == CAKeyInfo
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/CAKeyInfo/list'

        response.reset()


        populateValidParams(params)
        def CAKeyInfo = new CAKeyInfo(params)

        assert CAKeyInfo.save() != null

        // test invalid parameters in update
        params.id = CAKeyInfo.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/CAKeyInfo/edit"
        assert model.CAKeyInfoInstance != null

        CAKeyInfo.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/CAKeyInfo/show/$CAKeyInfo.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        CAKeyInfo.clearErrors()

        populateValidParams(params)
        params.id = CAKeyInfo.id
        params.version = -1
        controller.update()

        assert view == "/CAKeyInfo/edit"
        assert model.CAKeyInfoInstance != null
        assert model.CAKeyInfoInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/CAKeyInfo/list'

        response.reset()

        populateValidParams(params)
        def CAKeyInfo = new CAKeyInfo(params)

        assert CAKeyInfo.save() != null
        assert CAKeyInfo.count() == 1

        params.id = CAKeyInfo.id

        controller.delete()

        assert CAKeyInfo.count() == 0
        assert CAKeyInfo.get(CAKeyInfo.id) == null
        assert response.redirectedUrl == '/CAKeyInfo/list'
    }
}
