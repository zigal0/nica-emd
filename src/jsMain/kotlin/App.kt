package ru.mipt.npm.nica.emd

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.HTMLTag
import kotlinx.html.js.onClickFunction
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.Size
import react.*
import react.dom.*


val scope = MainScope()

val app = fc<Props> { props ->

    val (config, setConfig) = useState<ConfigFile>()
    val (menu, setMenu) = useState(true);

    val (currentPage, setCurrentPage) = useState<PageConfig>()
    // setCurrentPage(null) -- valid but causes too many re-renders here!

    val (EMDdata, setEMDdata) = useState<String>()


    useEffectOnce {
        scope.launch {
            setConfig(getConfig())
        }
    }

    /*  Box {
        attrs {
            sx = jso {
                flexGrow = FlexGrow(1.0)
                marginBottom = 25.px
            }

        }

        AppBar {
            attrs {
                position = AppBarPosition.static
            }

            Toolbar {

                Typography {
                    attrs {
                        sx = jso { flexGrow = FlexGrow(1.0) }
                        variant = "h6"
                        component = ReactHTML.div
                    }

                    + (config?.title ?: "EMS")
                }

                Button {
                    attrs {
                        color = ButtonColor.inherit
                    }

                    +"Login"
                }
            }
        }
    }
    */
    div("wrapper") {
        header() {
            nav() {
                div("menu_icon") {
                    div("mat-button") {
                        attrs.onClickFunction = {
                            setMenu(!menu)
                        }
                        span("app-icon") {}
                    }
                    div("menu_name animbut2") {
                        div("menu_name__text") {
                            key = "Home"
                            attrs.onClickFunction = {
                                setCurrentPage(null)
                            }
                            +"BM@N Event Metadata System"
                        }
                        dangerousSVG(SVGHeaderBubbles)
                    }
                }
                span("example-spacer") {}
                div("menu_name2") {
                    div("events_icon2") {}
                    div("login_block") {
                        div("wrap-form1 validate-input") {
                            input() {
                                attrs {
                                    placeholder = "Username"
                                }
                            } //<input type="text" #username  placeholder="Username" required>
                            span("focus-form1") {}
                            span("symbol-form1") {
                                div() {
                                    img(classes = "login-password-icon", src = "username.png") {  }
                                }
                            }
                        }
                        div("wrap-form1 validate-input") {
                            input() {
                                attrs {
                                    placeholder = "Password"
                                }
                            } //<input type="text" #password type="password"  placeholder="Password" required>
                            span("focus-form1") {}
                            span("symbol-form1") {
                                div() {
                                    img(classes = "login-password-icon", src = "password.png") {  }
                                }
                            }
                        }
                        div("but_login") {
                            Button {
                                attrs {
                                    +"Sign In"
                                    variant = ButtonVariant.contained
                                    size = Size.small
                                    onClick = {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        div("main-content") {
            if (menu) {
                div("sidenav") {
                    config?.pages?.forEach { item ->
                        div {
                            key = item.name
                            attrs.onClickFunction = {
                                setCurrentPage(item)
                                // Clear data for table
                                setEMDdata(null)
                            }
                            div("top__search") {
                                +item.name
                            }

                            inline fun RBuilder.custom(tagName: String, block: RDOMBuilder<HTMLTag>.() -> Unit) =
                                tag(block) {
                                    HTMLTag(
                                        tagName,
                                        it,
                                        mapOf(),
                                        null,
                                        true,
                                        false
                                    ) // I dont know yet what the last 3 params mean... to lazy to look it up
                                }

                            child(searchComponent) {
                                attrs.highlighted = (currentPage == item)
                            }
                        }
                    }
                }
            }
            if (currentPage == null) {
                child(homePage)
            } else {
                child(emdPage) {
                    attrs.pageConfig = currentPage
                    attrs.EMDdata = EMDdata
                    attrs.setEMDdata = { it: String? ->
                        setEMDdata(it)
                    }
                    attrs.condition_db = config?.condition_db
                }
            }
        }
        footer {
            div {
                a(href = "/") {
                    img(src = "home.png", classes = "home-icon") { }
                }
            }
            span("example-spacer") {}
            div {
                a(href = "https://bmn.jinr.ru/", target = "_blank") {
                    img(src = "favicon.png") {}
                }
            }
        }
    }
}

/*
            div("container-for-three") {
            // kotlin-react-dom-legacy is used here
            div("div-select-catalog") {
                Card {
                    attrs {
                        style = jso {
                            paddingLeft = 25.px
                            paddingRight = 25.px
                        }
                    }

                    ul {
                        config?.pages?.forEach { item ->
                            li {
                                key = item.name
                                attrs.onClickFunction = {
                                    setCurrentPage(item)
                                    // Clear data for table
                                    setEMDdata(null)
                                }
                                h4 {
                                    +"[${item.name}] ${item.api_url} "
                                }
                            }
                        }

                        li {
                            key = "Home"
                            attrs.onClickFunction = {
                                setCurrentPage(null)
                            }
                            h4 {
                                +"Home"
                            }
                        }

                    }
                }
            }

            if (currentPage == null) {
                child(homePage)
            } else {
                child(emdPage) {
                    attrs.pageConfig = currentPage
                    attrs.EMDdata = EMDdata
                    attrs.setEMDdata = { it: String? ->
                        setEMDdata(it)
                    }
                    attrs.condition_db = config?.condition_db
                }
            }
        } */


