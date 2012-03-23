package aaf.fr.foundation



import org.junit.*
import grails.test.mixin.*

@TestFor(ContactTypeController)
@Mock(ContactType)
class ContactTypeControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/contactType/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.contactTypeInstanceList.size() == 0
        assert model.contactTypeInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.contactTypeInstance != null
    }

    void testSave() {
        controller.save()

        assert model.contactTypeInstance != null
        assert view == '/contactType/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/contactType/show/1'
        assert controller.flash.message != null
        assert ContactType.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/contactType/list'


        populateValidParams(params)
        def contactType = new ContactType(params)

        assert contactType.save() != null

        params.id = contactType.id

        def model = controller.show()

        assert model.contactTypeInstance == contactType
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/contactType/list'


        populateValidParams(params)
        def contactType = new ContactType(params)

        assert contactType.save() != null

        params.id = contactType.id

        def model = controller.edit()

        assert model.contactTypeInstance == contactType
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/contactType/list'

        response.reset()


        populateValidParams(params)
        def contactType = new ContactType(params)

        assert contactType.save() != null

        // test invalid parameters in update
        params.id = contactType.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/contactType/edit"
        assert model.contactTypeInstance != null

        contactType.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/contactType/show/$contactType.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        contactType.clearErrors()

        populateValidParams(params)
        params.id = contactType.id
        params.version = -1
        controller.update()

        assert view == "/contactType/edit"
        assert model.contactTypeInstance != null
        assert model.contactTypeInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/contactType/list'

        response.reset()

        populateValidParams(params)
        def contactType = new ContactType(params)

        assert contactType.save() != null
        assert ContactType.count() == 1

        params.id = contactType.id

        controller.delete()

        assert ContactType.count() == 0
        assert ContactType.get(contactType.id) == null
        assert response.redirectedUrl == '/contactType/list'
    }
}
