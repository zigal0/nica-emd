import csstype.FlexGrow
import csstype.px
import kotlinext.js.jso
import react.*
import react.dom.*
import kotlinx.html.js.*
import kotlinx.coroutines.*
import mui.material.*
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML


val scope = MainScope()

val app = fc<Props> { props ->

    val (config, setConfig) = useState<ConfigFile>()

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
            nav(){
                div("menu_icon"){
                    div("mat-button"){
                        span("app-icon"){}
                    }
                    div("menu_name animbut2"){
                        div("menu_name__text"){
                            key = "Home"
                            attrs.onClickFunction = {
                                setCurrentPage(null)
                            }
                            +"BM@N Event Metadata System"
                        }
                    }
                }
                span("example-spacer"){}
                div("menu_name2"){
                    div("events_icon2"){}
                    div("login_block"){
                        div("wrap-form1 validate-input"){
                            input(){
                                attrs {
                                    placeholder="Username"
                                }
                            } //<input type="text" #username  placeholder="Username" required>
                            span("focus-form1"){}
                            span("symbol-form1"){
                                div(""){
                                    +"icon"
                                }
                            }
                        }
                        div("wrap-form1 validate-input"){
                            input(){
                                attrs {
                                placeholder="Password"
                                }                            
                            } //<input type="text" #password type="password"  placeholder="Password" required>
                            span("focus-form1"){}
                            span("symbol-form1"){
                                div(""){
                                    +"icon"
                                }
                            }
                        }
                        div("but_login"){
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
        div("main-content"){
            div("sidenav"){
                config?.pages?.forEach { item ->
                    div{
                        key = item.name
                        attrs.onClickFunction = {
                            setCurrentPage(item)
                            // Clear data for table
                            setEMDdata(null)
                        }
                        div("top__search"){
                            +"${item.name}"
                        }
                        div("search"){
                            svg(){
                                /*<svg class="search__svg" width="29" height="29" viewBox="0 0 29 29" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path fill-rule="evenodd" clip-rule="evenodd" d="M10.5643 21.5688V19.2384C10.5643 19.1251 10.5278 19.0321 10.455 18.9592C10.3822 18.8864 10.2891 18.85 10.1759 18.85H6.29193C6.17865 18.85 6.0856 18.8864 6.01278 18.9592C5.93995 19.0321 5.90354 19.1251 5.90354 19.2384V21.5688C5.90354 21.682 5.93995 21.7751 6.01278 21.8479C6.0856 21.9207 6.17865 21.9571 6.29193 21.9571H10.1759C10.2891 21.9571 10.3822 21.9207 10.455 21.8479C10.5278 21.7751 10.5643 21.682 10.5643 21.5688ZM10.5643 16.908V14.5777C10.5643 14.4644 10.5278 14.3713 10.455 14.2985C10.3822 14.2257 10.2891 14.1893 10.1759 14.1893H6.29193C6.17865 14.1893 6.0856 14.2257 6.01278 14.2985C5.93995 14.3713 5.90354 14.4644 5.90354 14.5777V16.908C5.90354 17.0213 5.93995 17.1144 6.01278 17.1872C6.0856 17.26 6.17865 17.2964 6.29193 17.2964H10.1759C10.2891 17.2964 10.3822 17.26 10.455 17.1872C10.5278 17.1144 10.5643 17.0213 10.5643 16.908ZM16.7785 21.5688V19.2384C16.7785 19.1251 16.7421 19.0321 16.6693 18.9592C16.5965 18.8864 16.5034 18.85 16.3902 18.85H12.5062C12.3929 18.85 12.2999 18.8864 12.2271 18.9592C12.1542 19.0321 12.1178 19.1251 12.1178 19.2384V21.5688C12.1178 21.682 12.1542 21.7751 12.2271 21.8479C12.2999 21.9207 12.3929 21.9571 12.5062 21.9571H16.3902C16.5034 21.9571 16.5965 21.9207 16.6693 21.8479C16.7421 21.7751 16.7785 21.682 16.7785 21.5688ZM10.5643 12.2474V9.91699C10.5643 9.80371 10.5278 9.71066 10.455 9.63784C10.3822 9.56501 10.2891 9.5286 10.1759 9.5286H6.29193C6.17865 9.5286 6.0856 9.56501 6.01278 9.63784C5.93995 9.71066 5.90354 9.80371 5.90354 9.91699V12.2474C5.90354 12.3606 5.93995 12.4537 6.01278 12.5265C6.0856 12.5993 6.17865 12.6357 6.29193 12.6357H10.1759C10.2891 12.6357 10.3822 12.5993 10.455 12.5265C10.5278 12.4537 10.5643 12.3606 10.5643 12.2474ZM16.7785 16.9081V14.5777C16.7785 14.4644 16.7421 14.3714 16.6693 14.2985C16.5965 14.2257 16.5034 14.1893 16.3902 14.1893H12.5062C12.3929 14.1893 12.2999 14.2257 12.2271 14.2985C12.1542 14.3714 12.1178 14.4644 12.1178 14.5777V16.9081C12.1178 17.0213 12.1542 17.1144 12.2271 17.1872C12.2999 17.26 12.3929 17.2965 12.5062 17.2965H16.3902C16.5034 17.2965 16.5965 17.26 16.6693 17.1872C16.7421 17.1144 16.7785 17.0213 16.7785 16.9081ZM22.9928 21.5688V19.2384C22.9928 19.1251 22.9564 19.0321 22.8836 18.9593C22.8108 18.8864 22.7177 18.85 22.6044 18.85H18.7205C18.6072 18.85 18.5142 18.8864 18.4414 18.9593C18.3685 19.0321 18.3321 19.1251 18.3321 19.2384V21.5688C18.3321 21.6821 18.3685 21.7751 18.4414 21.8479C18.5142 21.9208 18.6072 21.9572 18.7205 21.9572H22.6044C22.7177 21.9572 22.8108 21.9208 22.8836 21.8479C22.9564 21.7751 22.9928 21.6821 22.9928 21.5688ZM16.7785 12.2474V9.91702C16.7785 9.80374 16.7421 9.71069 16.6693 9.63787C16.5965 9.56504 16.5034 9.52863 16.3902 9.52863H12.5062C12.3929 9.52863 12.2999 9.56504 12.2271 9.63787C12.1542 9.71069 12.1178 9.80374 12.1178 9.91702V12.2474C12.1178 12.3607 12.1542 12.4537 12.2271 12.5265C12.2999 12.5994 12.3929 12.6358 12.5062 12.6358H16.3902C16.5034 12.6358 16.5965 12.5994 16.6693 12.5265C16.7421 12.4537 16.7785 12.3607 16.7785 12.2474ZM22.9928 16.9081V14.5777C22.9928 14.4645 22.9564 14.3714 22.8836 14.2986C22.8108 14.2258 22.7177 14.1893 22.6044 14.1893H18.7205C18.6072 14.1893 18.5142 14.2258 18.4414 14.2986C18.3685 14.3714 18.3321 14.4645 18.3321 14.5777V16.9081C18.3321 17.0214 18.3685 17.1144 18.4414 17.1873C18.5142 17.2601 18.6072 17.2965 18.7205 17.2965H22.6044C22.7177 17.2965 22.8108 17.2601 22.8836 17.1873C22.9564 17.1144 22.9928 17.0214 22.9928 16.9081ZM22.9928 12.2474V9.91702C22.9928 9.80374 22.9564 9.71069 22.8836 9.63787C22.8108 9.56504 22.7177 9.52863 22.6044 9.52863H18.7205C18.6072 9.52863 18.5142 9.56504 18.4414 9.63787C18.3685 9.71069 18.3321 9.80374 18.3321 9.91702V12.2474C18.3321 12.3607 18.3685 12.4537 18.4414 12.5265C18.5142 12.5994 18.6072 12.6358 18.7205 12.6358H22.6044C22.7177 12.6358 22.8108 12.5994 22.8836 12.5265C22.9564 12.4537 22.9928 12.3607 22.9928 12.2474ZM24.5464 8.36346V21.5688C24.5464 22.1029 24.3563 22.56 23.9759 22.9403C23.5956 23.3206 23.1385 23.5108 22.6044 23.5108H6.29194C5.7579 23.5108 5.30073 23.3206 4.92043 22.9403C4.54013 22.56 4.34998 22.1029 4.34998 21.5688V8.36346C4.34998 7.82942 4.54013 7.37225 4.92043 6.99195C5.30073 6.61164 5.7579 6.42149 6.29194 6.42149H22.6044C23.1385 6.42149 23.5956 6.61164 23.9759 6.99195C24.3563 7.37225 24.5464 7.82942 24.5464 8.36346Z"/>
                                </svg> */
                            }
                            div("search__name"){
                                +"Search Events"
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
        footer() {
            div(""){
                +"icon"
            }
            span("example-spacer"){}
            div(){ +"logo"} // <a href="https://bmn.jinr.ru"><img src="https://raw.githubusercontent.com/lokzzor/open/main/src/assets/favicon.png" /></a>
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


