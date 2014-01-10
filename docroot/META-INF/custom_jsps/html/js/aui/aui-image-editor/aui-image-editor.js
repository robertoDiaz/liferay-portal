YUI.add('aui-image-editor', function (A, NAME) {

/**
 * A Base for different ImageEditor implementations
 *
 * @module aui-image-editor
 * @submodule aui-image-editor-base
 */

var Lang = A.Lang,

    AObject = A.Object,

    _NAME = 'image-editor-base',

    BOUNDING_BOX = 'boundingBox',

    PROCESSORS_NODE_SELECTOR = '.image-editor-processors',
    IMAGE_NODE_SELECTOR = 'img',
    TOOLBAR_NODE_SELECTOR = '.toolbar',
    TRIGGER_NODE_SELECTOR = '.image-editor-trigger',

    IMAGE_TYPE_JPEG = 'image/jpeg',
    IMAGE_TYPE_PNG = 'image/png',

    REGEXP_IMAGE_EXT = /\.([^.]*)$/,
    REGEXP_JPG_IMAGE_EXT = /jpe?g$/i,

    STR_CLEAR = 'clear',
    STR_CLICK = 'click',
    STR_IMAGE_NODE = 'imageNode',
    STR_IMAGE_TYPE = 'imageType',
    STR_PROCESSORS = 'processors',
    STR_PROCESSORS_NODE = 'processorsNode',
    STR_REDO = 'redo',
    STR_RESET = 'reset',
    STR_SAVE = 'save',
    STR_TOOLBAR_NODE = 'toolbarNode',
    STR_TRIGGER_NODE = 'triggerNode',
    STR_UI = 'ui',
    STR_UNDO = 'undo',
    STR_VALUE = 'value',

    UNDO_DEPENDENCIES = ['gallery-undo', 'aui-image-editor-action'],

    /**
     * Base class for ImageEditor.
     *
     * @class A.ImageEditorBase
     */
    ImageEditorBase = A.Component.create({

        /**
         * Static property provides a string to identify the class.
         *
         * @property NAME
         * @type String
         * @static
         */
        NAME: _NAME,

        /**
         * Object hash, defining how attribute values have to be parsed from
         * markup contained in the ImageEditor's content box.
         *
         * @property HTML_PARSER
         * @type Object
         * @static
         */
        HTML_PARSER: {
            imageNode: IMAGE_NODE_SELECTOR,
            processorsNode: PROCESSORS_NODE_SELECTOR,
            toolbarNode: TOOLBAR_NODE_SELECTOR,
            triggerNode: TRIGGER_NODE_SELECTOR
        },

        /**
         * Static property used to define the default attribute
         * configuration for the ImageEditorBase.
         *
         * @property ATTRS
         * @type Object
         * @static
         */
        ATTRS: {

            /**
             * DOM node with the image to edit.
             * @type {Node}
             */
            imageNode: {
                setter: A.one
            },

            /**
             * MIME type of the image. If not defined, the editor will try to
             * guess based on the image source url.
             * @type {String}
             */
            imageType: {
                validator: Lang.isString
            },

            /**
             * Configuration of the processors available on this Editor.
             * @type {Object}
             */
            processors: {},

            /**
             * DOM node for the processors' User Interface.
             * @type {Node}
             */
            processorsNode: {
                setter: A.one
            },

            /**
             * DOM node with the Toolbar instance of the editor.
             * @type {Node}
             */
            toolbarNode: {
                setter: A.one
            },

            /**
             * DOM node with the Trigger button of the editor.
             * @type {Node}
             */
            triggerNode: {
                setter: A.one
            },

            /**
             * Undo configuration for the image editor. By default, undo is
             * enabled with unlimited number of levels.
             * @type {Object}
             */
            undo: {
                validator: Lang.isObject,
                value: {
                    enabled: true,
                    limit: 0
                }
            }
        },

        prototype: {

            PROCESSORS_NODE_TPL: '<div class="image-editor-processors">' +
                '<div class="image-processor-widgets"></div>' +
                '<div class="image-processor-toolbar"></div>' +
                '<div class="image-processor-status row-fluid">' +
                    '<button class="btn-cancel span1"><i class="icon-remove"></i></button>' +
                    '<div class="image-processor-info span10"></div>' +
                    '<button class="btn-ok span1"><i class="icon-ok"></i></button>'+
                '</div>' +
                '</div>',

            PROCESSOR_WIDGET_WRAPPER_TPL: '<div class="{cssClass}"></div>',

            TOOLBAR_NODE_TPL: '<div class="image-editor-toolbar hide">' +
                '<div class="btn-group">' +
                    '<button class="btn" data-action="undo"><i class="icon-arrow-left"></i></button>' +
                    '<button class="btn" data-action="redo"><i class="icon-arrow-right"></i></button>' +
                '</div>' +
                '<div class="btn-group btn-group-radio">' +
                    '<button class="btn" data-action="adjust"><i class="icon-adjust"></i></button>' +
                    '<button class="btn" data-action="filters"><i class="icon-picture"></i></button>' +
                    '<button class="btn" data-action="scale"><i class="icon-resize-small"></i></button>' +
                '</div>' +
                '</div>',

            TRIGGER_NODE_TPL: '<button class="image-editor-trigger image-editor-trigger-default">' +
                '<i class="icon-edit"></i>' +
                '</button>',

            /**
             * Construction logic executed during ImageEditorBase instantiation.
             * Lifecycle.
             *
             * @method initializer
             * @protected
             */
            initializer: function() {
                var instance = this,
                    imageExtension,
                    imageNode,
                    imageSrc,
                    imageType,
                    processors,
                    undo;

                undo = instance.get(STR_UNDO);

                if (undo.enabled) {
                    A.use(UNDO_DEPENDENCIES, A.bind('_createUndoManager', instance, undo));
                }

                imageNode = instance.get(STR_IMAGE_NODE);
                imageType = instance.get(STR_IMAGE_TYPE);

                if (!imageType) {
                    imageSrc = imageNode.get('src');
                    imageType = IMAGE_TYPE_PNG;

                    if (REGEXP_IMAGE_EXT.test(imageSrc)) {
                        imageExtension = REGEXP_IMAGE_EXT.exec(imageSrc)[1];

                        if(REGEXP_JPG_IMAGE_EXT.test(imageExtension)) {
                            imageType = IMAGE_TYPE_JPEG;
                        }
                    }

                    instance.set(STR_IMAGE_TYPE, imageType);
                }

                processors = instance.get(STR_PROCESSORS);

                if (!processors) {
                    processors = instance._getDefaultProcessors();

                    instance.set(STR_PROCESSORS, processors);
                }

                instance._actions = instance._getAvailableActions(processors);
            },

            /**
             * Render the ImageEditorBase component instance. Lifecycle.
             *
             * @method renderUI
             * @protected
             */
            renderUI: function() {
                var instance = this,
                    processorsNode,
                    toolbar,
                    toolbarNode,
                    triggerNode;

                triggerNode = instance.get(STR_TRIGGER_NODE);

                if (!triggerNode) {
                    triggerNode = instance._createTriggerNode();
                }

                toolbarNode = instance.get(STR_TOOLBAR_NODE);

                if (!toolbarNode) {
                    toolbarNode = instance._createToolbarNode();
                }
                else {
                    toolbar = instance._renderToolbar(toolbarNode);
                }

                processorsNode = instance.get(STR_PROCESSORS_NODE);

                if (!processorsNode) {
                    processorsNode = instance._createProcessorsNode();
                }

                instance._processorsNode = processorsNode;
                instance._processorStatusNode = processorsNode.one('.image-processor-status');
                instance._processorInfo = instance._processorStatusNode.one('.image-processor-info');
                instance._processorToolbarNode = processorsNode.one('.image-processor-toolbar');
                instance._processorWidgetsNode = processorsNode.one('.image-processor-widgets');
                instance._toolbar = toolbar;
                instance._toolbarNode = toolbarNode;
                instance._triggerNode = triggerNode;
            },

            /**
             * Bind the events on the ImageEditor UI. Lifecycle.
             *
             * @method bindUI
             * @protected
             */
            bindUI: function() {
                var instance = this,
                    clearButton,
                    eventHandles,
                    saveButton;

                eventHandles = [
                    A.Do.after(instance._resetUI, instance, STR_CLEAR, instance),
                    A.Do.after(instance._resetUI, instance, STR_SAVE, instance)
                ];

                eventHandles.push(instance._triggerNode.on(STR_CLICK, A.bind('_showControls', instance)));

                saveButton = instance._processorsNode.one('.btn-ok');

                if (saveButton) {
                    eventHandles.push(saveButton.on(STR_CLICK, A.bind(STR_SAVE, instance)));
                }

                clearButton = instance._processorsNode.one('.btn-cancel');

                if (clearButton) {
                    eventHandles.push(clearButton.on(STR_CLICK, A.bind(STR_CLEAR, instance)));
                }

                if (instance._toolbar) {
                    eventHandles.push(instance._bindToolbarUI());
                }

                instance._eventHandles = eventHandles;
            },

            /**
             * Destructor lifecycle implementation for the `HSVPalette` class.
             * Lifecycle.
             *
             * @method destructor
             * @protected
             */
            destructor: function() {
                var instance = this;

                (new A.EventHandle(instance._eventHandles)).detach();
            },

            /**
             * Discards any unsaved changes on the editor.
             *
             * @method clear
             */

            /**
             * Obtains a representation of the current image on the editor,
             * including unsaved changes.
             *
             * @method getImage
             * @return {Object} An Object representing the current state. The type
             * of object depends on the editor's implementation.
             */

            /**
             * Obtains a data:URL with a representation of the current image on the
             * editor, including unsaved changes.
             *
             * @method getImageData
             * @return {String} A data:URL with a representation of the image.
             */

            /**
             * Runs a processing action over the current state of the image in the editor.
             *
             * @method process
             * @param {A.ImageFilterBase|Object} processor The processor object to apply.
             * @param {Object} cfg Additional config parameters for the processor.
             * @param {Function} callback Callback for async processings.
             */

            /**
             * Puts an image on the editor.
             *
             * @method putImage
             * @param {Object} imageData An Object representing the image to set
             * inside the editor. The type of object depends on the editor's
             * implementation.
             * @param {Number} offsetX Horizontal offset.
             * @param {Number} offsetY Vertical offset.
             */

            /**
             * Resets the editor state discarding all the applied changes.
             *
             * @method reset
             */

            /**
             * Saves the current changes.
             *
             * @method save
             */

            /**
             * Binds the toolbar buttons with the available actions.
             *
             * @method _bindToolbarUI
             * @private
             * @return {EventHandle} The toolbar event handle.
             */
            _bindToolbarUI: function() {
                var instance = this,
                    eventHandle;

                eventHandle = instance._toolbarNode.delegate(
                    STR_CLICK,
                    function(event) {
                        action = instance._actions[event.currentTarget.getAttribute('data-action')];

                        if (Lang.isFunction(action)) {
                            action();
                        }
                    },
                    'button'
                );

                return eventHandle;
            },

            /**
             * [_createProcessorUIWrapper description]
             *
             * @method _createProcessorUIWrapper
             * @private
             * @param  {A.ImageFilterBase|Object} processor The processor object.
             * @param  {String} cssClass CSS Class for the wrapper node.
             * @return {Node} A Node wrapper for the processor's UI.
             */
            _createProcessorUIWrapper: function(processor, cssClass) {
                var instance = this,
                    processorUI,
                    wrapper;

                wrapper = instance._processorWidgetsNode.appendChild(
                    Lang.sub(
                        instance.PROCESSOR_WIDGET_WRAPPER_TPL, {
                            cssClass: cssClass
                        }
                    )
                );

                processorUI = A.instanceOf(processor, A.Base) ? processor.get(STR_UI) : processor.ui;

                if (Lang.isObject(processorUI)) {
                    if (Lang.isFunction(processorUI.fn)) {
                        widget = new processorUI.fn(processorUI.cfg).render(wrapper);

                        widget.on('valueChange', function(event) {
                            instance.process(processor, event.newVal);
                        });
                    }
                }

                wrapper.setData('widget', widget);

                return wrapper;
            },

            /**
             * Creates the processors UI node.
             *
             * @method _createTriggerNode
             * @private
             * @return {Node} Node for the Editor's processors UI.
             */
            _createProcessorsNode: function() {
                var instance = this,
                    processorsNode;

                processorsNode = A.Node.create(instance.PROCESSORS_NODE_TPL);

                instance.get(BOUNDING_BOX).append(processorsNode);

                processorsNode.hide();

                return processorsNode;
            },

            /**
             * Creates the toolbar node.
             *
             * @method _createTriggerNode
             * @private
             * @return {Node} Node for the Editor's toolbar.
             */
            _createToolbarNode: function() {
                var instance = this,
                    toolbarNode;

                toolbarNode = A.Node.create(instance.TOOLBAR_NODE_TPL);

                instance.get(BOUNDING_BOX).append(toolbarNode);

                return toolbarNode;
            },

            /**
             * Creates the trigger node.
             *
             * @method _createTriggerNode
             * @private
             * @return {Node} Node for the Editor's trigger.
             */
            _createTriggerNode: function() {
                var instance = this,
                    triggerNode;

                triggerNode = A.Node.create(instance.TRIGGER_NODE_TPL);

                instance.get(BOUNDING_BOX).append(triggerNode);

                return triggerNode;
            },

            /**
             * Initializes the UndoManager once it's been loaded.
             *
             * @method _createUndoManager
             * @private
             * @param {Object} config ImageEditor undo configuration settings.
             */
            _createUndoManager: function(config) {
                var instance = this;

                instance._undoManager = new A.UndoManager(
                    {
                        limit: config.limit
                    }
                );
            },

            /**
             * Creates a map of available actions based on the editor
             * capabilities and the instance processors configuration.
             *
             * @method _getAvailableActions
             * @private
             * @param  {Object} processors The processors configuration object.
             * @return {Object} A map exposing the available actions based on
             * the editor capabilities and the configured processors.
             */
            _getAvailableActions: function(processors) {
                var instance = this,
                    actions;

                actions = {
                    'clear': A.bind(STR_CLEAR, instance),
                    'redo': A.bind(STR_REDO, instance),
                    'reset': A.bind(STR_RESET, instance),
                    'save': A.bind(STR_SAVE, instance),
                    'undo': A.bind(STR_UNDO, instance)
                };

                AObject.each(
                    processors,
                    function(item, index) {
                        actions[index] = A.bind('_renderProcessorToolbar', instance, item);
                    }
                );

                return actions;
            },

            /**
             * Creates the basic processors configuration.
             *
             * @method _getDefaultProcessors
             * @private
             * @return {Object} An Object with the default processor configuration.
             */
            _getDefaultProcessors: function() {
                var processors;

                processors = {
                    'adjust': {
                        'brightness': new A.ImageAdjustFilter({mode: 'brightness'}),
                        'contrast': new A.ImageAdjustFilter({mode: 'contrast'}),
                        'saturation': new A.ImageAdjustFilter({mode: 'saturation'})
                    },
                    'filters': {
                        'grayscale': new A.ImageGrayscaleFilter(),
                        'sepia': new A.ImageSepiaFilter(),
                        'polaroid': new A.ImagePolaroidFilter(),
                        'invert': new A.ImageInvertFilter()
                    },
                    'scale': {
                        'mirror-x': new A.ImageScaleFilter({scaleX: -1}),
                        'mirror-y': new A.ImageScaleFilter({scaleY: -1})
                    }
                };

                return processors;
            },

            /**
             * Gets
             *
             * @method _getProcessorUIWrapper
             * @private
             * @param  {A.ImageFilterBase|Object} processor The processor object.
             * @param  {String} processorName Name for the given processor.
             * @return {Node} A Node wrapper for the processor's UI.
             */
            _getProcessorUIWrapper: function(processor, processorName) {
                var instance = this,
                    cssClass,
                    widget,
                    wrapper;

                cssClass = 'image-processor-ui-' + processorName;

                wrapper = instance._processorWidgetsNode.one('.' + cssClass);

                if (!wrapper) {
                    wrapper = instance._createProcessorUIWrapper(processor, cssClass);
                }

                widget = wrapper.getData('widget');

                if (widget) {
                    widget.reset();

                    // https://github.com/yui/yui3/issues/1537
                    if (widget.get(STR_VALUE)) {
                        widget.reset(STR_VALUE);
                    }
                }

                return wrapper;
            },

            /**
             * Fires a state change event.
             *
             * @method _notifyStateChange
             * @protected
             * @param  {String} state The state change.
             * @param  {Object} prevVal Image data before the change.
             * @param  {Object} newVal Image data after the change.
             */
            _notifyStateChange: function(state, prevVal, newVal) {
                var instance = this,
                    eventName;

                eventName = 'imageProcessor:' + state;

                instance.fire(
                   eventName,
                   {
                       newVal: newVal,
                       prevVal: prevVal
                   }
                );

                if (instance._undoManager && state === 'save') {
                    instance._undoManager.add(
                        new A.ImageEditorAction(
                            {
                                editor: instance,
                                newVal: newVal,
                                prevVal: prevVal
                            }
                        )
                    );
                }
            },

            undo: function() {
                var instance = this;

                if (instance._undoManager) {
                    instance._undoManager.undo();
                }
            },

            redo: function() {
                var instance = this;

                if (instance._undoManager) {
                    instance._undoManager.redo();
                }
            },

            /**
             * Initializes and renders the Toolbar of the ImageEditor.
             *
             * @method _renderToolbar
             * @private
             * @param {Node} toolbarNode The Node to use as the toolbar boundingBox.
             * @return {A.Toolbar} The Toolbar for the editor.
             */
            _renderToolbar: function(toolbarNode) {
                var toolbar;

                toolbar = new A.Toolbar(
                    {
                        boundingBox: toolbarNode
                    }
                ).render();

                return toolbar;
            },

            /**
             * Renders a toolbar for a given processors group configuration.
             *
             * @method _renderProcessorToolbar
             * @private
             * @param  {Object} processors Processor configuration.
             */
            _renderProcessorToolbar: function(processors) {
                var instance = this,
                    processorButtons = [];

                instance.clear();

                instance._processorsNode.show();
                instance._processorToolbarNode.show();
                instance._toolbar.hide();
                instance._triggerNode.hide();

                AObject.each(
                    processors,
                    function(item, index) {
                        processorButtons.push({
                            cssClass: 'btn-image-processor btn-image-processor-' + index,
                            label: index,
                            on: {
                                click: A.bind('_renderProcessorUI', instance, item, index)
                            }
                        });
                    }
                );

                instance._processorToolbar = new A.Toolbar(
                    {
                        boundingBox: instance._processorToolbarNode,
                        children: processorButtons
                    }
                ).render();
            },

            /**
             * Renders a given processor's UI.
             *
             * @method _renderProcessorUI
             * @private
             * @param  {A.ImageFilterBase|Object} processor The processor object.
             */
            _renderProcessorUI: function(processor, processorName) {
                var instance = this,
                    processorUI,
                    wrapper;

                if (instance._currentWrapper) {
                    instance._currentWrapper.hide();
                }

                instance._processorStatusNode.show();
                instance._processorWidgetsNode.show();
                instance._processorToolbarNode.hide();

                processorUI = A.instanceOf(processor, A.Base) ? processor.get(STR_UI) : processor.ui;

                if (!processorUI) {
                    instance.process(processor);
                }
                else {
                    wrapper = instance._getProcessorUIWrapper(processor, processorName);

                    wrapper.show();
                }

                instance._currentWrapper = wrapper;
            },

            /**
             * Restores the ImageEditor UI to the initial state.
             *
             * @method _resetUI
             * @private
             */
            _resetUI: function() {
                var instance = this;

                if (instance._processorStatusNode) {
                    instance._processorStatusNode.hide();
                }

                if (instance._processorWidgetsNode) {
                    instance._processorWidgetsNode.hide();
                }

                instance._triggerNode.show();
            },

            /**
             * Shows the first-level edition controls. It also creates, renders
             * and binds the toolbar if it was not already set.
             *
             * @method _showControls
             * @private
             */
            _showControls: function() {
                var instance = this;

                instance._triggerNode.hide();

                if (!instance._toolbar) {
                    instance._toolbar = instance._renderToolbar(instance._toolbarNode);
                    instance._eventHandles.push(instance._bindToolbarUI());
                }

                instance._toolbar.show();
            }
        }
    });

A.ImageEditorBase = ImageEditorBase;


}, '2.0.0', {
    "requires": [
        "aui-image-color-filter-base",
        "aui-image-filter-adjust",
        "aui-image-filter-grayscale",
        "aui-image-filter-invert",
        "aui-image-filter-polaroid",
        "aui-image-filter-sepia",
        "aui-image-blur-filter",
        "aui-image-scale-filter",
        "aui-toolbar",
        "timers"
    ],
    "skinnable": true
});
