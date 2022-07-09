package ru.mipt.npm.nica.emd

import react.dom.div
import kotlinx.coroutines.launch
import kotlinx.html.DIV
import react.dom.RDOMBuilder
import mui.material.TextField
import mui.material.*
import mui.material.Size
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.onChange

external interface DictionaryPageProps : Props {
    var SWdata: Array<SoftwareVersion>?
    var setSWdata: (Array<SoftwareVersion>?) -> Unit
    var Storagedata: Array<Storage>?
    var setStoragedata: (Array<Storage>?) -> Unit
    var config: ConfigFile?
    var username: String
    var password: String
}

val dictionary = fc<DictionaryPageProps> { props ->
    val (storage, setStorage) = useState<String>()
    val (SWver, setSWver) = useState<String>()

    useEffectOnce {
        scope.launch {
            props.setSWdata(getSoftwareVersions(props.config, props.username, props.password))
            props.setStoragedata(getStorages(props.config, props.username, props.password))
        }
    }

    div("dictionary") {
        div("dictionary__top") {
            div("dictionary__top__card") {
                dangerousSVG(SVGCloudForDict)
                div("dictionary__back__input") {
                    TextField {
                        attrs {
                            id = "storage_name"
                            value = storage ?: ""
                            variant = FormControlVariant.outlined
                            label = ReactNode("Storage Name")
                            onChange = {
                                val newStorage = (it.target as HTMLInputElement).value
                                setStorage(newStorage)
                            }
                        }
                    }
                }
                Button {
                    attrs {
                        +"Add"
                        variant = ButtonVariant.contained
                        size = Size.small
                        onClick = {
                            scope.launch {
                                if (!storage.isNullOrEmpty()) {
                                    postStorage(storage, props.config, props.username, props.password)
                                    props.setStoragedata(getStorages(props.config, props.username, props.password))
                                }
                            }
                            setStorage("")  // clear in the input
                        }
                    }
                }
            }
            div("dictionary__top__card") {
                dangerousSVG(SVGSWforDict)
                div("dictionary__back__input") {
                    TextField {
                        attrs {
                            id = "software_version"
                            value = SWver ?: ""
                            variant = FormControlVariant.outlined
                            label = ReactNode("Software Version")
                            onChange = {
                                val newSW = (it.target as HTMLInputElement).value
                                setSWver(newSW)
                            }
                        }
                    }
                }
                Button {
                    attrs {
                        +"Add"
                        variant = ButtonVariant.contained
                        size = Size.small
                        onClick = {
                            scope.launch {
                                if (!SWver.isNullOrEmpty()) {
                                    postSoftwareVersion(SWver, props.config, props.username, props.password)
                                    props.setSWdata(getSoftwareVersions(props.config, props.username, props.password))
                                }
                            }
                            setSWver("")  // clear in the input
                        }
                    }
                }
            }
        }
        div("dictionary__bottom") {
            child(storageTable) {
                attrs.content = props.Storagedata
            }
            child(softwareTable) {
                attrs.content = props.SWdata
            }
        }
    }
}
