import { createApp } from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'
import router from './router'
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faThermometerHalf, faTint,faLightbulb, faLeaf, faPhone } from "@fortawesome/free-solid-svg-icons";

library.add(faThermometerHalf, faTint, faLightbulb, faLeaf, faPhone);

loadFonts()

createApp(App)
  .use(vuetify).use(router).component("font-awesome-icon", FontAwesomeIcon)
  .mount('#app')
