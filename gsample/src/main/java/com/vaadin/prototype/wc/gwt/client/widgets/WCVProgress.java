package com.vaadin.prototype.wc.gwt.client.widgets;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.Widgets;
import static com.vaadin.prototype.wc.gwt.client.widgets.WCUtils.getAttrFloatValue;
import static com.vaadin.prototype.wc.gwt.client.widgets.WCUtils.getAttrValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.js.JsExport;
import com.google.gwt.core.client.js.JsProperty;
import com.google.gwt.core.client.js.JsType;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VProgressBar;
import com.vaadin.prototype.wc.gwt.client.WC;
import com.vaadin.prototype.wc.gwt.client.html.HTMLElement;
import com.vaadin.prototype.wc.gwt.client.html.HTMLEvents;
import com.vaadin.prototype.wc.gwt.client.html.HTMLShadow;

@JsExport
@JsType
public class WCVProgress extends HTMLElement.Prototype implements
        HTMLElement.LifeCycle.Created,
        HTMLElement.LifeCycle.Attached,
        HTMLElement.LifeCycle.Changed,
        Handler {

    public static final String TAG = "v-progress";

    private VProgressBar widget;
    private HTMLEvents changeEvent;
    private HTMLElement container;
    private HTMLElement style;
    private Panel shadowPanel;
    private boolean initialized = false;
    private String theme = "valo";

    public WCVProgress() {
        // FIXME: If there is no default constructor JsInterop does not export anything
    }

    @Override
    public void createdCallback() {
        style = WC.create("style");

        widget = new VProgressBar();

        changeEvent = WC.document.createEvent("HTMLEvents");
        changeEvent.initEvent("change", false, false);

        container = WC.create("div");
        readAttributes();
    }

    /*
     * TODO: common stuff for exporting other widgets
     */
    private void initWidgetSystem() {
        if (!initialized) {
            initialized = true;
            Widget elementWidget = $(this).widget();
            if (elementWidget == null) {
                elementWidget = $(this).as(Widgets).panel().widget();
            }
            elementWidget.addAttachHandler(this);

            HTMLShadow shadow = this.createShadowRoot();
            shadow.appendChild(style);
            shadow.appendChild(container);

            Panel shadowPanel = $(container).as(Widgets).panel().widget();
            shadowPanel.add(widget);
        }
    }

    @Override
    public void attachedCallback() {
        initWidgetSystem();
    }


    @Override
    public void attributeChangedCallback() {
        readAttributes();
    }

    private void readAttributes() {
        widget.setState(getAttrFloatValue(this, "value", 0));
        theme = getAttrValue(this, "theme", "valo");
        style.innerText("@import url('" + GWT.getModuleBaseURL() + "../../themes/" + theme + "/styles.css')");
        style.innerText("@import url('VAADIN/themes/" + theme + "/styles.css')");
        container.setAttribute("class", theme);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        // TODO: Do something with shadowPanel, right now
        // gQuery creates a new root-panel so it does not
        // have any parent, but we should maintain the widget
        // hierarchy someway.
    }

    public void jsPropertyValue() {
    }

    @JsProperty public void setValue(double value) {
        widget.setState((float)value);
    }

    @JsProperty public double getValue() {
        return widget.getState();
    }
}
