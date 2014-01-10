var A = AUI();

var DOCUMENT = A.config.doc;

var canvasTest = function() {
	var canvas = DOCUMENT && DOCUMENT.createElement('canvas'),
        useFlash = A.config.defaultImageEditorEngine && A.config.defaultImageEditorEngine == 'flash';

    return !useFlash && (canvas && canvas.getContext && canvas.getContext('2d'));
};

var swfTest = function() {
	var canvas = DOCUMENT && DOCUMENT.createElement('canvas'),
    	useFlash = A.config.defaultImageEditorEngine && A.config.defaultImageEditorEngine == 'flash';

	return useFlash || !(canvas && canvas.getContext && canvas.getContext('2d'));
};

A.applyConfig(
	{
		modules: {
			'aui-image-editor': {
				path: 'aui-image-editor/aui-image-editor.js',
				requires: [
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
				skinnable: true
			},
			'aui-image-editor-canvas': {
				path: 'aui-image-editor-canvas/aui-image-editor-canvas.js',
				condition: {
					trigger: "aui-image-editor",
					test: canvasTest
				}
			},
			'aui-image-editor-canvas-default': {
				path: 'aui-image-editor-canvas-default/aui-image-editor-canvas-default.js',
				condition: {
					trigger: 'aui-image-editor',
					test: canvasTest
				}
			},
			'aui-image-editor-swf': {
				path: 'aui-image-editor-swf/aui-image-editor-swf.js',
				condition: {
					trigger: 'aui-image-editor',
					test: swfTest
				}
			},
			'aui-image-editor-swf-default': {
				path: 'aui-image-editor-swf-default/aui-image-editor-swf-default.js',
				condition: {
					trigger: 'aui-image-editor',
					test: swfTest
				}
			},
			'aui-image-filter': {
				path: 'aui-image-filter/aui-image-filter.js'
			},
			'aui-image-color-filter-base': {
				path: 'aui-image-color-filter-base/aui-image-color-filter-base.js',
				requires: [
					'aui-image-filter'
				]
			},
			'aui-image-filter-grayscale': {
				path: 'aui-image-filter-grayscale/aui-image-filter-grayscale.js'
			},
			'aui-image-filter-invert': {
				path: 'aui-image-filter-invert/aui-image-filter-invert.js'
			},
			'aui-image-filter-polaroid': {
				path: 'aui-image-filter-polaroid/aui-image-filter-polaroid.js'
			},
			'aui-image-filter-sepia': {
				path: 'aui-image-filter-sepia/aui-image-filter-sepia.js'
			},
			'aui-image-blur-filter': {
				path: 'aui-image-blur-filter/aui-image-blur-filter.js'
			},
			'aui-image-scale-filter': {
				path: 'aui-image-scale-filter/aui-image-scale-filter.js'
			},
			'aui-image-filter-adjust': {
				path: 'aui-image-filter-adjust/aui-image-filter-adjust.js',
				requires: [
					'aui-image-color-filter-base',
					'slider'
				]
			},
			'aui-image-editor-action': {
				path: 'aui-image-editor-action/aui-image-editor-action.js'
			}
		}
	}
);