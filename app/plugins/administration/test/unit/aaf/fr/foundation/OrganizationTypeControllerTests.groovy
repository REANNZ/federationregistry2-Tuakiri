package aaf.fr.foundation



import org.junit.*
import grails.test.mixin.*

@TestFor(OrganizationTypeController)
@Mock(OrganizationType)
class OrganizationTypeControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/organizationType/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.organizationTypeInstanceList.size() == 0
        assert model.organizationTypeInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.organizationTypeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.organizationTypeInstance != null
        assert view == '/organizationType/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/organizationType/show/1'
        assert controller.flash.message != null
        assert OrganizationType.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/organizationType/list'


        populateValidParams(params)
        def organizationType = new OrganizationType(params)

        assert organizationType.save() != null

        params.id = organizationType.id

        def model = controller.show()

        assert model.organizationTypeInstance == organizationType
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/organizationType/list'


        populateValidParams(params)
        def organizationType = new OrganizationType(params)

        assert organizationType.save() != null

        params.id = organizationType.id

        def model = controller.edit()

        assert model.organizationTypeInstance == organizationType
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/organizationType/list'

        response.reset()


        populateValidParams(params)
        def organizationType = new OrganizationType(params)

        assert organizationType.save() != null

        // test invalid parameters in update
        params.id = organizationType.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/organizationType/edit"
        assert model.organizationTypeInstance != null

        organizationType.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/organizationType/show/$organizationType.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        organizationType.clearErrors()

        populateValidParams(params)
        params.id = organizationType.id
        params.version = -1
        controller.update()

        assert view == "/organizationType/edit"
        assert model.organizationTypeInstance != null
        assert model.organizationTypeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/organizationType/list'

        response.reset()

        populateValidParams(params)
        def organizationType = new OrganizationType(params)

        assert organizationType.save() != null
        assert OrganizationType.count() == 1

        params.id = organizationType.id

        controller.delete()

        assert OrganizationType.count() == 0
        assert OrganizationType.get(organizationType.id) == null
        assert response.redirectedUrl == '/organizationType/list'
    }
}
