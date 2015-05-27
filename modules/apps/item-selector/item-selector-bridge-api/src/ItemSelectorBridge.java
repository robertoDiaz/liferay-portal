

import com.liferay.document.library.item.selector.web.DLItemSelectorCriterion;
import com.liferay.item.selector.ItemSelector;
import com.liferay.portal.kernel.item.selector.PortalItemSelector;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;

import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Roberto Díaz
 */
@Component
public class ItemSelectorBridge implements PortalItemSelector {

	@Override
	public PortletURL getItemSelectorURL(
		PortletResponse portletResponse, String itemSelectedCallback,
		long groupId, Class<?>... desiredReturnTypes) {

		DLItemSelectorCriterion dlItemSelectorCriterion =
			new DLItemSelectorCriterion(
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, groupId, "image",
				PropsValues.DL_FILE_ENTRY_PREVIEW_IMAGE_MIME_TYPES, false);

		return _itemSelector.getItemSelectorURL(
			portletResponse, itemSelectedCallback, dlItemSelectorCriterion);
	}

	@Reference
	public void setItemSelector(ItemSelector itemSelector) {
		_itemSelector = itemSelector;
	}

	private ItemSelector _itemSelector;

}