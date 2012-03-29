package grails.plugins.federatedgrails



import org.junit.*
import grails.test.mixin.*

@TestFor(PermissionController)
@Mock(Permission)
class PermissionControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/permission/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.permissionInstanceList.size() == 0
        assert model.permissionInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.permissionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.permissionInstance != null
        assert view == '/permission/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/permission/show/1'
        assert controller.flash.message != null
        assert Permission.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/permission/list'


        populateValidParams(params)
        def permission = new Permission(params)

        assert permission.save() != null

        params.id = permission.id

        def model = controller.show()

        assert model.permissionInstance == permission
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/permission/list'


        populateValidParams(params)
        def permission = new Permission(params)

        assert permission.save() != null

        params.id = permission.id

        def model = controller.edit()

        assert model.permissionInstance == permission
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/permission/list'

        response.reset()


        populateValidParams(params)
        def permission = new Permission(params)

        assert permission.save() != null

        // test invalid parameters in update
        params.id = permission.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/permission/edit"
        assert model.permissionInstance != null

        permission.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/permission/show/$permission.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        permission.clearErrors()

        populateValidParams(params)
        params.id = permission.id
        params.version = -1
        controller.update()

        assert view == "/permission/edit"
        assert model.permissionInstance != null
        assert model.permissionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/permission/list'

        response.reset()

        populateValidParams(params)
        def permission = new Permission(params)

        assert permission.save() != null
        assert Permission.count() == 1

        params.id = permission.id

        controller.delete()

        assert Permission.count() == 0
        assert Permission.get(permission.id) == null
        assert response.redirectedUrl == '/permission/list'
    }
}
