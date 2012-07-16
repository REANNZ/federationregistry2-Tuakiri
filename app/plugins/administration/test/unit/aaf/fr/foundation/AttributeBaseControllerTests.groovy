package aaf.fr.foundation



import org.junit.*
import grails.test.mixin.*

@TestFor(AttributeBaseController)
@Mock(AttributeBase)
class AttributeBaseControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/attributeBase/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.attributeBaseInstanceList.size() == 0
        assert model.attributeBaseInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.attributeBaseInstance != null
    }

    void testSave() {
        controller.save()

        assert model.attributeBaseInstance != null
        assert view == '/attributeBase/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/attributeBase/show/1'
        assert controller.flash.message != null
        assert AttributeBase.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/attributeBase/list'


        populateValidParams(params)
        def attributeBase = new AttributeBase(params)

        assert attributeBase.save() != null

        params.id = attributeBase.id

        def model = controller.show()

        assert model.attributeBaseInstance == attributeBase
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/attributeBase/list'


        populateValidParams(params)
        def attributeBase = new AttributeBase(params)

        assert attributeBase.save() != null

        params.id = attributeBase.id

        def model = controller.edit()

        assert model.attributeBaseInstance == attributeBase
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/attributeBase/list'

        response.reset()


        populateValidParams(params)
        def attributeBase = new AttributeBase(params)

        assert attributeBase.save() != null

        // test invalid parameters in update
        params.id = attributeBase.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/attributeBase/edit"
        assert model.attributeBaseInstance != null

        attributeBase.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/attributeBase/show/$attributeBase.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        attributeBase.clearErrors()

        populateValidParams(params)
        params.id = attributeBase.id
        params.version = -1
        controller.update()

        assert view == "/attributeBase/edit"
        assert model.attributeBaseInstance != null
        assert model.attributeBaseInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/attributeBase/list'

        response.reset()

        populateValidParams(params)
        def attributeBase = new AttributeBase(params)

        assert attributeBase.save() != null
        assert AttributeBase.count() == 1

        params.id = attributeBase.id

        controller.delete()

        assert AttributeBase.count() == 0
        assert AttributeBase.get(attributeBase.id) == null
        assert response.redirectedUrl == '/attributeBase/list'
    }
}
