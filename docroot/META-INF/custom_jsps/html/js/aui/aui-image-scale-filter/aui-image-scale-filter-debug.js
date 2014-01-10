YUI.add('aui-image-scale-filter', function (A, NAME) {

var Lang = A.Lang,

    _NAME = 'image-scale-filter',

    STR_CANVAS = 'canvas',
    STR_HEIGHT = 'height',
    STR_SCALE_X = 'scaleX',
    STR_SCALE_Y = 'scaleY',
    STR_WIDTH = 'width',
    STR_2D = '2d',

    /**
     * A dimension-scale filter.
     *
     * @class A.ImageScaleFilter
     * @extends A.ImageFilterBase
     * @constructor
     */
    ImageScaleFilter = A.Base.create(_NAME, A.ImageFilterBase, [],
    {
        /**
         * Construction logic executed during ImageScaleFilter
         * instantiation. Lifecycle.
         *
         * @method initializer
         * @protected
         */
        initializer: function() {
            var instance = this;

            instance._bufferCanvas = A.config.doc.createElement(STR_CANVAS);
            instance._scaleCanvas = A.config.doc.createElement(STR_CANVAS);
        },

        /**
         * Applies the filter transformation to the given data.
         *
         * @method process
         * @param {Object} drawingContext Object with the relevant drawing
         * context information.
         * @param {Object} cfg Configuration parameters for the filter.
         */
        process: function(drawingContext, cfg) {
            var instance = this,
                bitmapData,
                bufferCanvas,
                bufferContext,
                scaleCanvas,
                scaleContext,
                scaleX,
                scaleY,
                width,
                height;

            scaleX = instance.get(STR_SCALE_X);
            scaleY = instance.get(STR_SCALE_Y);

            if (cfg) {
                if (cfg.scaleX) {
                    scaleX = cfg.scaleX;
                }

                if (cfg.scaleY) {
                    scaleY = cfg.scaleY;
                }
            }

            bitmapData = drawingContext.bitmapData;
            width = bitmapData.width,
            height = bitmapData.height;

            bufferCanvas = instance._bufferCanvas,
            bufferContext = bufferCanvas.getContext(STR_2D),

            scaleCanvas = instance._scaleCanvas,
            scaleContext = scaleCanvas.getContext(STR_2D),

            bufferCanvas.setAttribute(STR_WIDTH, width);
            bufferCanvas.setAttribute(STR_HEIGHT, height);

            bufferContext.putImageData(bitmapData, 0, 0);

            scaleCanvas.setAttribute(STR_WIDTH, width);
            scaleCanvas.setAttribute(STR_HEIGHT, height);

            scaleContext.scale(scaleX, scaleY);
            scaleContext.drawImage(bufferCanvas, (- (1 - scaleX) / 2) * width, (- (1 - scaleY) / 2) * height);

            bitmapData.data.set(scaleContext.getImageData(0, 0, width, height).data);
        }
    }, {
        /**
         * Static property provides a string to identify the class.
         *
         * @property NAME
         * @type String
         * @static
         */
        NAME: _NAME,

        /**
         * Static property used to define the default attribute
         * configuration for the ImageScaleFilter.
         *
         * @property ATTRS
         * @type Object
         * @static
         */
        ATTRS: {

            /**
             * Scale ratio to apply along the horizontal axis of the image.
             *
             * @attribute scaleX
             * @type {Number}
             * @default 1
             */
            scaleX: {
                validator: Lang.isNumber,
                value: 1
            },

            /**
             * Scale ratio to apply along the vertical axis of the image.
             *
             * @attribute scaleY
             * @type {Number}
             * @default 1
             */
            scaleY: {
                validator: Lang.isNumber,
                value: 1
            },

            /**
             * Configuration for the flash fallback implementation of the
             * filter.
             *
             * @attribute swfCfg
             * @type {Object}
             */
            swfCfg: {
                readonly: true,
                value: {
                    processFn: 'scaleMatrixFilter',
                    params: [
                        STR_SCALE_X,
                        STR_SCALE_Y
                    ]
                }
            }
        }
    });

A.ImageScaleFilter = ImageScaleFilter;

}, '2.0.0', {"requires": ["aui-image-filter"]});
