package aaf.fr.foundation



import org.junit.*
import grails.test.mixin.*

@TestFor(MonitorTypeController)
@Mock(MonitorType)
class MonitorTypeControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/monitorType/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.monitorTypeInstanceList.size() == 0
        assert model.monitorTypeInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.monitorTypeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.monitorTypeInstance != null
        assert view == '/monitorType/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/monitorType/show/1'
        assert controller.flash.message != null
        assert MonitorType.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/monitorType/list'


        populateValidParams(params)
        def monitorType = new MonitorType(params)

        assert monitorType.save() != null

        params.id = monitorType.id

        def model = controller.show()

        assert model.monitorTypeInstance == monitorType
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/monitorType/list'


        populateValidParams(params)
        def monitorType = new MonitorType(params)

        assert monitorType.save() != null

        params.id = monitorType.id

        def model = controller.edit()

        assert model.monitorTypeInstance == monitorType
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/monitorType/list'

        response.reset()


        populateValidParams(params)
        def monitorType = new MonitorType(params)

        assert monitorType.save() != null

        // test invalid parameters in update
        params.id = monitorType.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/monitorType/edit"
        assert model.monitorTypeInstance != null

        monitorType.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/monitorType/show/$monitorType.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        monitorType.clearErrors()

        populateValidParams(params)
        params.id = monitorType.id
        params.version = -1
        controller.update()

        assert view == "/monitorType/edit"
        assert model.monitorTypeInstance != null
        assert model.monitorTypeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/monitorType/list'

        response.reset()

        populateValidParams(params)
        def monitorType = new MonitorType(params)

        assert monitorType.save() != null
        assert MonitorType.count() == 1

        params.id = monitorType.id

        controller.delete()

        assert MonitorType.count() == 0
        assert MonitorType.get(monitorType.id) == null
        assert response.redirectedUrl == '/monitorType/list'
    }
}
