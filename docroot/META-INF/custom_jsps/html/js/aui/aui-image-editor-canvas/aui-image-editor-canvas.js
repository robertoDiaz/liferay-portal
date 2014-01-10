YUI.add('aui-image-editor-canvas', function (A, NAME) {

var Lang = A.Lang,

    _NAME = 'image-editor-canvas',

    STR_HEIGHT = 'height',
    STR_WIDTH = 'width',

    /**
     * An HTML5 canvas implementation of an ImageEditor.
     *
     * @class A.ImageProcessorCanvas
     * @extends A.ImageEditorBase
     * @constructor
     */
    ImageEditorCanvas = A.Base.create(_NAME, A.ImageEditorBase, [], {

        /**
         * Construction logic executed during ImageEditorCanvas instantiation
         * Lifecycle.
         *
         * @protected
         */
        initializer: function() {
            var instance = this,
                imageNode = instance.get('imageNode'),
                srcNode = instance.get('srcNode'),
                canvas, context,
                width, height;

            if (srcNode && imageNode) {
                width = imageNode.get(STR_WIDTH);
                height = imageNode.get(STR_HEIGHT);

                canvas = A.config.doc.createElement('canvas');
                context = canvas.getContext('2d');

                canvas.setAttribute(STR_WIDTH, width);
                canvas.setAttribute(STR_HEIGHT, height);

                context.drawImage(imageNode.getDOMNode(), 0, 0);

                imageNode.insert(canvas, 'after');

                instance._canvas = canvas;
                instance._ctx = context;

                instance._initialData = context.getImageData(0, 0, width, height);

                instance._currentData = instance._cloneImageData(instance._initialData);
                instance._previewData = instance._cloneImageData(instance._initialData);
            }
        },

        /**
         * Discards any unsaved changes on the editor.
         *
         * @method clear
         */
        clear: function() {
            var instance = this;

            instance._ctx.putImageData(instance._currentData, 0, 0);

            instance._notifyStateChange('clear');
        },

        /**
         * Obtains a representation of the current image on the editor,
         * including unsaved changes.
         *
         * @method getImage
         * @return {ImageData} An ImageData representing the current state.
         */
        getImage: function() {
            var instance = this,
                height,
                width;

            width = instance._canvas.getAttribute(STR_WIDTH);
            height = instance._canvas.getAttribute(STR_HEIGHT);

            return instance._cloneImageData(instance._ctx.getImageData(0, 0, width, height));
        },

        /**
         * Obtains a data:URL with a representation of the current image on the
         * editor, including unsaved changes.
         *
         * @method getImageData
         * @return {String} A data:URL with a representation of the image.
         */
        getImageData: function() {
            var instance = this;

            return instance._canvas.toDataURL(instance.get('imageType'));
        },

        /**
         * Runs a processing action over the current state of the image in the editor.
         *
         * @method process
         * @param {A.ImageFilterBase|Object} processor The processor object to apply.
         * @param {Object} cfg Additional config parameters for the processor.
         * @param {Function} callback Callback for async processings.
         */
        process: function(processor, cfg, callback) {
            var instance = this,
                async;

            async = A.instanceOf(processor, A.Base) ? processor.get('async') : processor.async;

            instance.putImage(instance._currentData);

            instance._previewData = instance._cloneImageData(instance._currentData);

            if (Lang.isFunction(processor.process)) {
                processor.process(
                    {
                        bitmapData: instance._previewData,
                        statusNode: instance._processorInfo,
                        _ctx: instance._ctx,
                        _canvas: instance._canvas
                    },
                    cfg,
                    A.bind('_onProcessComplete', instance, async, callback)
                );

                if (!async) {
                    instance._onProcessComplete(async, callback);
                }
            }
        },

        /**
         * Puts an image on the editor.
         *
         * @method putImage
         * @param {ImageData} imageData An ImageData representing the image.
         * @param {Number} offsetX Horizontal offset.
         * @param {Number} offsetY Vertical offset.
         */
        putImage: function(imageData, offsetX, offsetY) {
            var instance = this;

            offsetX = offsetX || 0,
            offsetY = offsetY || 0;

            instance._currentData = instance._cloneImageData(imageData);
            instance._previewData = instance._cloneImageData(imageData);

            instance._ctx.putImageData(imageData, offsetX, offsetY);
        },

        /**
         * Resets the editor state discarding all the applied changes.
         *
         * @method reset
         */
        reset: function() {
            var instance = this;

            instance._ctx.putImageData(instance._initialData, 0, 0);
            instance._currentData = instance._cloneImageData(instance._initialData);

            instance._notifyStateChange('reset');
        },

        /**
         * Saves the current changes.
         *
         * @method save
         */
        save: function() {
            var instance = this;

            instance._notifyStateChange
            (
                'save',
                instance._cloneImageData(instance._currentData),
                instance.getImage()
            );

            instance._currentData = instance.getImage();
        },

        /**
         * Completes the process action and invokes the optional callback.
         *
         * @method _onProcessComplete
         * @private
         * @param  {boolean} async If the process was asynchronous.
         * @param  {Function} callback Optional callback to invoke upon
         * processing completion.
         */
        _onProcessComplete: function(async, callback) {
            var instance = this;

            if (!async) {
                instance._ctx.putImageData(instance._previewData, 0, 0);
            }

            if (callback) {
                callback();
            }
        },

        /**
         * Clones an existing ImageData object.
         *
         * @protected
         * @method _cloneImageData
         * @param  {ImageData} imageData The ImageData object to be cloned.
         * @return {ImageData} A copy of the given ImageData object.
         */
        _cloneImageData: function(imageData) {
            var instance = this,
                clonedImageData = instance._ctx.createImageData(imageData.width, imageData.height),
                clonedData = clonedImageData.data,
                originalData = imageData.data,
                i, l;

            if (clonedData.set) {
                clonedData.set(imageData.data);
            } else {
                l = originalData.length;

                for (i = 0; i < l; i++) {
                    clonedData[i] = originalData[i];
                }
            }

            return clonedImageData;
        }

    }, {

    });

A.ImageEditorCanvas = ImageEditorCanvas;

}, '2.0.0');
