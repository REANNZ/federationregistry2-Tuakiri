(function() {
    if (typeof(YUI) === 'undefined') { alert('Error! YUI3 library is not available') }

    YUI.add('consolelayout', function(Y) {
        var Layout = function() { Layout.superclass.constructor.apply(this, arguments); }
        Layout.NAME = "consolelayout";
        Layout.ATTRS = {
            units:{
                value :undefined
            }
        }
        Layout.INPUT_CLASS = Y.ClassNameManager.getClassName(Layout.NAME, "value");

        Y.extend(Layout, Y.Widget, {
            _layout:undefined,
            initializer: function() {
                Y.log(this + '.initializer()', 'debug', 'layout');
            },
            renderUI:function() {
                Y.log(this + '.renderUI()', 'debug', 'layout')
                try {
                    this._layout = new YAHOO.widget.Layout({units:this.get('units')})
                    this._layout.render()
                    Y.log(this + '.renderUI() - rendered layout', 'debug', 'layout')
                } catch(e) {
                    Y.log(this + '.renderUI() - e: ' + e, 'error', 'layout')
                }
            },
            _bindCollapse:{},
            bindUI:function() {
                /*  var left = this._layout.getUnitByPosition('left')
                 left.on('collapse', Y.bind(function() {
                 if (!this._bindCollapse['left']) {
                 Y.get(left._clip).on('click', function() {left.toggle()});
                 Y.get(left._clip).set('title', 'click to expand')
                 Y.get(left._clip).setStyle('cursor', 'pointer')
                 Y.get(left._clip).get('firstChild').setStyle('visibility', 'hidden')
                 this._bindCollapse['left'] = true
                 }
                 }, this))*/
                var pos = 'bottom'
                var unit = this._layout.getUnitByPosition(pos)
                if (unit) {
                    unit.on('collapse', Y.bind(function() {
                        if (!this._bindCollapse[pos]) {
                            Y.get(unit._clip).on('click', function() {unit.toggle()});
                            Y.get(unit._clip).set('title', 'click to expand')
                            Y.get(unit._clip).setStyle('cursor', 'pointer')
                            Y.get(unit._clip).get('firstChild').setStyle('visibility', 'hidden')
                            this._bindCollapse[pos] = true
                        }
                    }, this))
                }
            },
            destructor : function() {
                Y.log(this + '.destructor()', 'debug', 'layout');
            }
        })
        Y.ConsoleLayout = Layout
    }, '3.0.0pr2', {requires:['widget']})
})()