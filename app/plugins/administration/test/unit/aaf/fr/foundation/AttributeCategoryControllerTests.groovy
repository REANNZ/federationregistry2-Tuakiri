package aaf.fr.foundation



import org.junit.*
import grails.test.mixin.*

@TestFor(AttributeCategoryController)
@Mock(AttributeCategory)
class AttributeCategoryControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/attributeCategory/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.attributeCategoryInstanceList.size() == 0
        assert model.attributeCategoryInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.attributeCategoryInstance != null
    }

    void testSave() {
        controller.save()

        assert model.attributeCategoryInstance != null
        assert view == '/attributeCategory/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/attributeCategory/show/1'
        assert controller.flash.message != null
        assert AttributeCategory.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/attributeCategory/list'


        populateValidParams(params)
        def attributeCategory = new AttributeCategory(params)

        assert attributeCategory.save() != null

        params.id = attributeCategory.id

        def model = controller.show()

        assert model.attributeCategoryInstance == attributeCategory
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/attributeCategory/list'


        populateValidParams(params)
        def attributeCategory = new AttributeCategory(params)

        assert attributeCategory.save() != null

        params.id = attributeCategory.id

        def model = controller.edit()

        assert model.attributeCategoryInstance == attributeCategory
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/attributeCategory/list'

        response.reset()


        populateValidParams(params)
        def attributeCategory = new AttributeCategory(params)

        assert attributeCategory.save() != null

        // test invalid parameters in update
        params.id = attributeCategory.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/attributeCategory/edit"
        assert model.attributeCategoryInstance != null

        attributeCategory.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/attributeCategory/show/$attributeCategory.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        attributeCategory.clearErrors()

        populateValidParams(params)
        params.id = attributeCategory.id
        params.version = -1
        controller.update()

        assert view == "/attributeCategory/edit"
        assert model.attributeCategoryInstance != null
        assert model.attributeCategoryInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/attributeCategory/list'

        response.reset()

        populateValidParams(params)
        def attributeCategory = new AttributeCategory(params)

        assert attributeCategory.save() != null
        assert AttributeCategory.count() == 1

        params.id = attributeCategory.id

        controller.delete()

        assert AttributeCategory.count() == 0
        assert AttributeCategory.get(attributeCategory.id) == null
        assert response.redirectedUrl == '/attributeCategory/list'
    }
}
