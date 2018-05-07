"use strict";



define('varwatch/adapters/application', ['exports', 'ember-data', 'ember-simple-auth/mixins/data-adapter-mixin'], function (exports, _emberData, _dataAdapterMixin) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTAdapter.extend(_dataAdapterMixin.default, {
    authorizer: 'authorizer:varwatchoauth2-bearer',
    //host: 'http://134.245.63.237:8080',
    //host: 'https://varwatch.ikmb.uni-kiel.de:8443',
    namespace: 'varwatch/api',
    handleResponse: function handleResponse(status) {
      if (status === 406) {
        this.get('session').invalidate();
      } else {
        return this._super.apply(this, arguments);
      }
    }
    /*buildURL() {
      let res = this._super(...arguments);
      //console.log(res);
      //console.log('^^^^^');
      return res;
    },*/
    /*  handleResponse(status, headers, payload, requestData){
        console.log("fdddddddddddddddddfffdfdfdffrequest status answer");
        console.log(status);
        console.log(payload);
        console.log(requestData);
        return this._super(...arguments);
      }*/
    /*ajax: function(url, method, hash) {
      console.log(hash);
      hash = hash || {};
      hash.crossDomain = true;
      hash.xhrFields = {withCredentials: true};
      return this._super(url, method, hash);
    }*/

  });
});
define('varwatch/adapters/dataset', ['exports', 'varwatch/adapters/application', 'ember-data'], function (exports, _application, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _application.default.extend({
    urlForFindAll: function urlForFindAll(id, modelName, snapshot) {
      return '/varwatch/api/information/datasets/';
    },
    urlForFindRecord: function urlForFindRecord(id, modelName, snapshot) {
      return '/varwatch/api/information/datasets/' + id;
    },
    handleResponse: function handleResponse(status, headers, payload, requestData) {
      //console.log("DATASETADAPTER status answer");
      if (status === 500) return _emberData.default.NotFoundError;
      var statusurl = '/varwatch/api/status/datasets/';
      var url = requestData.url;
      var newpayload = payload;
      //console.log("url: "+url);
      //let pathpos =  url.search('/');
      //console.log("pathpos: "+pathpos);
      //console.log(url.substring(pathpos, statusurl.length));
      if (url.substring(0, statusurl.length) === statusurl) {
        var end = url.length;
        if (url.substring(end - 4, end) === "/all") {
          end -= 4;
          newpayload = {};
          var id = url.substring(statusurl.length, end);
          //console.log("using id: "+id);
          newpayload.id = id;
          newpayload.payload = payload;
        } else {
          //console.log("mysterious status dataset request: "+url);
          var _id = url.substring(statusurl.length, end);
          //console.log("using id: "+id);
          payload.id = _id;
        }
      }
      return this._super(status, headers, newpayload, requestData);
    }
  });
});
define('varwatch/adapters/match', ['exports', 'varwatch/adapters/application'], function (exports, _application) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _application.default.extend({
    urlForFindAll: function urlForFindAll(id, modelName, snapshot) {
      return '/varwatch/api/matching/new';
    },
    urlForFindRecord: function urlForFindRecord(id, modelName, snapshot) {
      return '/varwatch/api/matching/matches/' + id;
    }
  });
});
define("varwatch/adapters/status", ["exports", "varwatch/adapters/application"], function (exports, _application) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _application.default.extend({
    urlForFindRecord: function urlForFindRecord(id, modelName, snapshot) {
      return "/varwatch/api/status/datasets/" + id;
    },
    handleResponse: function handleResponse(status, headers, payload, requestData) {
      //console.log("DATASETADAPTER status answer");
      if (status === 500) return DS.NotFoundError;
      var statusurl = "/varwatch/api/status/datasets/";
      var url = requestData.url;
      var newpayload = payload;
      //console.log("url: "+url);
      //let pathpos =  url.search('/');
      //console.log("pathpos: "+pathpos);
      //console.log(url.substring(pathpos, statusurl.length));
      if (url.substring(0, statusurl.length) === statusurl) {
        var end = url.length;
        if (url.substring(end - 4, end) === "/all") {
          end -= 4;
          newpayload = {};
          var id = url.substring(statusurl.length, end);
          //console.log("using id: "+id);
          newpayload.id = id;
          newpayload.payload = payload;
        } else {
          //console.log("mysterious status dataset request: "+url);
          var _id = url.substring(statusurl.length, end);
          //console.log("using id: "+id);
          payload.id = _id;
        }
      }
      return this._super(status, headers, newpayload, requestData);
    }
  });
});
define('varwatch/adapters/userinfo', ['exports', 'varwatch/adapters/application'], function (exports, _application) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _application.default.extend({
    /*buildURL(modelName){ //(modelName, id, snapshot, requestType, query)
      if (modelName==='userinfo') {return "/varwatch/api/registration/user/information";}
      else {return this._super(...arguments);}
    },*/
    urlForFindAll: function urlForFindAll(modelName, snapshot) {
      //(modelName, id, snapshot, requestType, query)
      if (modelName === 'userinfo') {
        return "/varwatch/api/registration/user/information";
      } else {
        return this._super.apply(this, arguments);
      }
    },
    urlForUpdateRecord: function urlForUpdateRecord(id, modelName, snapshot) {
      if (modelName === 'userinfo') {
        return "/varwatch/api/user/update";
      } else {
        return this._super.apply(this, arguments);
      }
    },

    //shoehorn current userinfo model into update response since it is not returned by the server
    updateRecord: function updateRecord(store, type, snapshot) {
      return this._super.apply(this, arguments).then(function () {
        var dat = {};
        store.serializerFor(type.modelName).serializeIntoHash(dat, type, snapshot);
        dat['mail'] = snapshot.id;
        return dat;
      });
    }
  });
});
define('varwatch/adapters/variant', ['exports', 'varwatch/adapters/application'], function (exports, _application) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _application.default.extend({
    urlForFindRecord: function urlForFindRecord(id, modelName, snapshot) {
      return '/varwatch/api/information/variants/' + id;
    }
  }
  //handleResponse(status, headers, payload, requestData){
  /*
  //console.log("Variant ADAPTER status answer");
  let statusurl = `/varwatch/api/status/variants/`;
  let url = requestData.url;
  //let pathpos =  url.search('/');
  if (url.substring(0,statusurl.length)===statusurl)
  {
    let end = url.length;
    let newpayload={};
    if (url.substring(end-4,end)==="/all") {end -=4;}
    let id= url.substring(statusurl.length, end);
    //console.log("variantstatus using id: "+id);
    newpayload.id=id;
    newpayload.payload=payload;
    return this._super(status, headers, newpayload, requestData);
  } else {
    
  }*/
  //return this._super(...arguments);
  //}
  );
});
define('varwatch/adapters/variantstatus', ['exports', 'varwatch/adapters/application'], function (exports, _application) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _application.default.extend({
    urlForFindRecord: function urlForFindRecord(id, modelName, snapshot) {
      return '/varwatch/api/status/' + id;
    },
    handleResponse: function handleResponse(status, headers, payload, requestData) {
      //console.log("Variant ADAPTER status answer");
      var statusurl = '/varwatch/api/matching/variants/';
      var url = requestData.url;
      //let pathpos =  url.search('/');
      if (url.substring(0, statusurl.length) === statusurl) {
        var end = url.length;
        var i = url.lastIndexOf('/');
        //let newpayload={};
        var id = url.substring(i + 1, end);
        //console.log("match using id: "+id);
        payload.id = id;
        //newpayload.payload=payload;
        return this._super(status, headers, payload, requestData);
      } else {
        return this._super(status, headers, payload, requestData);
      }
    }
  });
});
define('varwatch/app', ['exports', 'varwatch/resolver', 'ember-load-initializers', 'varwatch/config/environment', 'varwatch/reopens/text-field', 'varwatch/models/custom-inflector-rules'], function (exports, _resolver, _emberLoadInitializers, _environment, _textField) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var App = Ember.Application.extend({
    modulePrefix: _environment.default.modulePrefix,
    podModulePrefix: _environment.default.podModulePrefix,
    Resolver: _resolver.default
  });

  (0, _emberLoadInitializers.default)(App, _environment.default.modulePrefix);

  exports.default = App;
});
define("varwatch/authenticators/varwatchoauth2", ["exports", "ember-simple-auth/authenticators/oauth2-password-grant"], function (exports, _oauth2PasswordGrant) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _oauth2PasswordGrant.default.extend({
    serverTokenEndpoint: "/varwatch/api/registration/token",
    //serverTokenRevocationEndpoint: "/varwatch/api/registration/logout",
    clientId: "varwatch",
    refreshAccessTokens: false,
    makeRequest: function makeRequest(url, data) {
      //console.log(data);
      data.client_secret = "varwatch";
      data.client_id = "varwatch";
      //console.log(data);
      return this._super(url, data);
    }
  });
});
define('varwatch/authorizers/varwatchoauth2-bearer', ['exports', 'ember-simple-auth/authorizers/oauth2-bearer'], function (exports, _oauth2Bearer) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _oauth2Bearer.default.extend();
});
define("varwatch/components/-lf-get-outlet-state", ["exports", "liquid-fire/components/-lf-get-outlet-state"], function (exports, _lfGetOutletState) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _lfGetOutletState.default;
    }
  });
});
define('varwatch/components/bootstrap-fileselect', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    change: function change(event) {
      //console.log("files selected");
      //console.log(event.target);
      //console.log(event.target.files);
      if (event.target.files && event.target.files[0]) {
        //console.log(event.target.files[0].name);
        //this.set('file', event.target.files[0]);
        this.sendAction('fileChanged', event.target.files[0]);
      }
    }
  });
});
define('varwatch/components/elide-cell', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    tagName: 'td',
    //font: 
    displayText: Ember.computed('text', function () {
      var t = "";
      if (this.get('text')) {
        t = this.get('text');
      }
      return this.getTextShorter(t, this.get('maxWidth'), true);
    }),
    textElided: Ember.computed('text', 'displayText', function () {
      return this.get('text') !== this.get('displayText');
    }),
    init: function init() {
      //this.set("text", "");
      this._super.apply(this, arguments);
    },
    getTextWidth: function getTextWidth(text) {
      var canvas = document.createElement("canvas");
      var context = canvas.getContext("2d");
      context.font = '14px sans-serif';
      var metrics = context.measureText(text);
      return metrics.width;
    },
    getTextShorter: function getTextShorter(text, maxl, firstcall) {
      var l = this.getTextWidth(text + (firstcall ? '' : '...'));
      if (l <= maxl) {
        return text + (firstcall ? '' : '...');
      } else {
        var t = text.slice(0, -1);
        return this.getTextShorter(t, maxl, false);
      }
    },
    didInsertElement: function didInsertElement() {
      Ember.$('[data-toggle="tooltip"]').tooltip();
      this._super.apply(this, arguments);
    }
  });
});
define('varwatch/components/ensembl-data', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    tagName: 'span',
    classNames: ['ensembl-data-object'],
    didRender: function didRender() {
      Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
      this._super.apply(this, arguments);
    },
    didInsertElement: function didInsertElement() {
      Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
      this._super.apply(this, arguments);
    },

    ensemblLoaded: Ember.observer('ensembl.description', function () {
      //console.log("a change has been detected")
      Ember.run.later(this, function () {
        //console.log("running wild");
        //console.log(this.get("ensembl.description"));
        //console.log(Ember.$('[data-toggle="tooltip"]'));
        Ember.$('[data-toggle="tooltip"]').tooltip("destroy");
        Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
      }, 500);
      //Ember.$('[data-toggle="tooltip"]').tooltip({container: "body"});
    })
  });
});
define('varwatch/components/form-group', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    classNames: ['form-group'],
    classNameBindings: ['hasError:has-error']
  });
});
define('varwatch/components/from-elsewhere', ['exports', 'ember-elsewhere/components/from-elsewhere'], function (exports, _fromElsewhere) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _fromElsewhere.default;
    }
  });
});
define('varwatch/components/help-page', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    didInsertElement: function didInsertElement() {
      this._super.apply(this, arguments);
      //console.log($('#myAffix'));
      Ember.$('#myAffix').affix({
        offset: {
          top: 148,
          bottom: 70
        }
      });
      Ember.$('body').scrollspy({ target: '#myScrollspy' });
    }
  });
});
define('varwatch/components/hgvs-list', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    classNames: ['panel', 'panel-default', 'no-bottom-margin'],
    classNameBindings: ['makeDanger:panel-danger'],
    didInsertElement: function didInsertElement() {
      this._super.apply(this, arguments);
      Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
    }
  });
});
define('varwatch/components/home-links', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    classNames: ['container', 'flex1', 'flex'],
    didInsertElement: function didInsertElement() {
      this._super.apply(this, arguments);
      Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
      //console.log("insert links")
    },
    willDestroyElement: function willDestroyElement() {
      this._super.apply(this, arguments);
      this.$('[data-toggle="tooltip"]').tooltip('destroy');
      //console.log("destroy links")
    }
  });
});
define('varwatch/components/hpo-browser', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    hpo: Ember.inject.service(),
    actions: {
      searchHpos: function searchHpos(term) {
        this.set('searchResults', this.get('hpo').searchHpos(term));
        Ember.run.schedule('afterRender', function () {
          Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
        });
      },
      hpoChanged: function hpoChanged(hpoterm) {
        this.setNewHpo(hpoterm);
      },
      clearSearchResults: function clearSearchResults() {
        this.set('searchResults', []);
      },
      toggleShowBrowser: function toggleShowBrowser() {
        if (!this.get('showBrowser') && this.get('selectedHpo') === undefined) {
          this.setNewHpo('HP:0000118');
        }
        this.set('showBrowser', !this.get('showBrowser'));
        Ember.run.schedule('afterRender', function () {
          Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
        });
      }
    },
    init: function init() {
      this.set('showBrowser', false);
      this._super.apply(this, arguments);
    },
    setNewHpo: function setNewHpo(hpoterm) {
      this.set('selectedHpo', this.get('hpo').getHpo(hpoterm));
      this.set('hpoChildren', this.get('hpo').getChildHpos(hpoterm));
      this.set('hpoParents', this.get('hpo').getParentHpos(hpoterm));
      this.set('hpoSiblings', this.get('hpo').getSiblingHpos(hpoterm));
      Ember.run.schedule('afterRender', function () {
        Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
      });
    }
  });
});
define("varwatch/components/illiquid-model", ["exports", "liquid-fire/components/illiquid-model"], function (exports, _illiquidModel) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _illiquidModel.default;
    }
  });
});
define('varwatch/components/labeled-radio-button', ['exports', 'ember-radio-button/components/labeled-radio-button'], function (exports, _labeledRadioButton) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _labeledRadioButton.default;
    }
  });
});
define("varwatch/components/liquid-bind", ["exports", "liquid-fire/components/liquid-bind"], function (exports, _liquidBind) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _liquidBind.default;
    }
  });
});
define("varwatch/components/liquid-child", ["exports", "liquid-fire/components/liquid-child"], function (exports, _liquidChild) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _liquidChild.default;
    }
  });
});
define("varwatch/components/liquid-container", ["exports", "liquid-fire/components/liquid-container"], function (exports, _liquidContainer) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _liquidContainer.default;
    }
  });
});
define("varwatch/components/liquid-if", ["exports", "liquid-fire/components/liquid-if"], function (exports, _liquidIf) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _liquidIf.default;
    }
  });
});
define("varwatch/components/liquid-measured", ["exports", "liquid-fire/components/liquid-measured"], function (exports, _liquidMeasured) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _liquidMeasured.default;
    }
  });
  Object.defineProperty(exports, "measure", {
    enumerable: true,
    get: function () {
      return _liquidMeasured.measure;
    }
  });
});
define("varwatch/components/liquid-outlet", ["exports", "liquid-fire/components/liquid-outlet"], function (exports, _liquidOutlet) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _liquidOutlet.default;
    }
  });
});
define("varwatch/components/liquid-spacer", ["exports", "liquid-fire/components/liquid-spacer"], function (exports, _liquidSpacer) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _liquidSpacer.default;
    }
  });
});
define('varwatch/components/liquid-sync', ['exports', 'liquid-fire/components/liquid-sync'], function (exports, _liquidSync) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _liquidSync.default;
    }
  });
});
define("varwatch/components/liquid-unless", ["exports", "liquid-fire/components/liquid-unless"], function (exports, _liquidUnless) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _liquidUnless.default;
    }
  });
});
define("varwatch/components/liquid-versions", ["exports", "liquid-fire/components/liquid-versions"], function (exports, _liquidVersions) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, "default", {
    enumerable: true,
    get: function () {
      return _liquidVersions.default;
    }
  });
});
define('varwatch/components/match-type', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    tagName: 'span',
    classNames: ['match-type'],
    didRender: function didRender() {
      Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
      this._super.apply(this, arguments);
    },
    didInsertElement: function didInsertElement() {
      Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
      this._super.apply(this, arguments);
    },

    matchTypeObserver: Ember.observer('matchTypeDescription', function () {
      Ember.run.later(this, function () {
        Ember.$('[data-toggle="tooltip"]').tooltip("destroy");
        Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
      }, 500);
    }),
    matchTypeDescription: Ember.computed('matchType', function () {
      switch (this.get('matchType')) {
        case 'perfect':
          return "Identical Variant Match";
        case 'codon':
          return "Variant Match in the same Codon";
        case 'lof':
          return "Loss-of-Function in the Same Gene ";
        case 'gene':
          return "Variant Match in the Same Gene ";
        default:
          return this.get('matchType');
      }
    })
  });
});
define('varwatch/components/modal-target', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    modalAnimation: modalAnimation
  });


  function modalAnimation() {
    return this.lookup('explode').call(this, {
      pick: '.modal-background',
      use: ['fade', { maxOpacity: 0.50 }]
    }, {
      pickNew: '.modal-container',
      use: ['to-down', { duration: 500 }]
    }, {
      pickOld: '.modal-container',
      use: ['to-up', { duration: 500 }]
    });
  }
});
define('varwatch/components/multiple-from-elsewhere', ['exports', 'ember-elsewhere/components/multiple-from-elsewhere'], function (exports, _multipleFromElsewhere) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _multipleFromElsewhere.default;
    }
  });
});
define('varwatch/components/radio-button-input', ['exports', 'ember-radio-button/components/radio-button-input'], function (exports, _radioButtonInput) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _radioButtonInput.default;
    }
  });
});
define('varwatch/components/radio-button', ['exports', 'ember-radio-button/components/radio-button'], function (exports, _radioButton) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _radioButton.default;
    }
  });
});
define('varwatch/components/registration-dialog', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    actions: {
      registerUser: function registerUser() {
        //console.log("register!");
        var props = this.getProperties('firstName', 'lastName', 'mail', 'institution', 'phone', 'address', 'postalCode', 'city', 'country');
        //console.log(props);
        //console.log(this.getProperty)
        //props.address = this.getProperties('address', 'postalCode', 'city', 'country');
        props.password = this.getProperties('password');
        var data = JSON.stringify(props);
        if (this.get('firstNameNotOk') || this.get('lastNameNotOk') || this.get('institutionNotOk') || this.get('emailNotOk') || this.get('emailsDontMatch') || this.get('passwordNotOk') || this.get('passrepeatNotOk') || this.get('addressNotOk') || this.get('cityNotOk') || this.get('postalCodeNotOk') || this.get('countryNotOk')) {
          this.set('showFormErrors', true);
          //console.log(data);
          Ember.run.schedule('afterRender', function () {
            var errorElements = document.getElementsByClassName('has-error');
            if (errorElements.length > 0) {
              errorElements[0].scrollIntoView();
            }
            //console.log(errorElements);
            //console.log(errorElements[0]);
          });
        } else {
          var self = this;
          Ember.$.ajax({
            url: "/varwatch/api/registration/user",
            type: "POST",
            data: data,
            processData: false,
            contentType: "application/json; charset=UTF-8",
            error: function error(a1, a2, a3) {
              //console.log("ajax error");
              //console.log(a1);
              //console.log(a2);
              //console.log(a3);
              if (a1.responseJSON && a1.responseJSON.description && a1.responseJSON.description === "User exists already in database") {
                self.set('showFormErrors', true);
                self.set("existingEmail", self.get("mail"));
              } else {
                self.set('errorMessage', "Internal server error");
              }
            },
            success: function success(a1, a2, a3) {
              //console.log("ajax success");
              //console.log(a1);
              //console.log(a2);
              //console.log(a3);
              var mailaddy = self.get("mail");
              self.resetForm();
              self.set('successMessage', "Your Account Data has been sucessfully submitted. You will receive an eMail containing the Registration Form shortly.");
            }
          });
        }
      },
      resetForm: function resetForm() {
        this.resetForm();
      },
      closeModalDialog: function closeModalDialog() {
        this.set('errorMessage', "");
        this.set('successMessage', "");
      }
    },
    init: function init() {
      this.resetForm();
      this._super.apply(this, arguments);
    },

    firstNameNotOk: Ember.computed('firstName', function () {
      return this.get('firstName').trim() === '';
    }),
    lastNameNotOk: Ember.computed('lastName', function () {
      return this.get('lastName').trim() === '';
    }),
    institutionNotOk: Ember.computed('institution', function () {
      return this.get('institution').trim() === '';
    }),
    passwordNotOk: Ember.computed('password', function () {
      return this.get('password') === '';
    }),
    passrepeatNotOk: Ember.computed('passrepeat', 'password', function () {
      return this.get('passrepeat') !== this.get('password');
    }),
    emailNotOk: Ember.computed('mail', function () {
      return !/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(this.get('mail').trim());
    }),
    emailExists: Ember.computed('mail', 'existingEmail', function () {
      return this.get('mail') === this.get('existingEmail');
    }),
    emailsDontMatch: Ember.computed('mail', 'mailrepeat', function () {
      return this.get('mail') !== this.get('mailrepeat');
    }),
    addressNotOk: Ember.computed('address', function () {
      return this.get('address').trim() === '';
    }),
    postalCodeNotOk: Ember.computed('postalCode', function () {
      return this.get('postalCode').trim() === '';
    }),
    cityNotOk: Ember.computed('city', function () {
      return this.get('city').trim() === '';
    }),
    countryNotOk: Ember.computed('country', function () {
      return this.get('country').trim() === '';
    }),
    didRender: function didRender() {
      this._super.apply(this, arguments);
    },
    resetForm: function resetForm() {
      this.set('firstName', "");
      this.set('lastName', "");
      this.set('mail', "");
      this.set('mailrepeat', "");
      this.set('institution', "");
      this.set('phone', "");
      this.set('password', "");
      this.set('passrepeat', "");
      this.set('address', "");
      this.set('postalCode', "");
      this.set('city', "");
      this.set('country', "");
      this.set('showFormErrors', false);
      this.set('existingEmail', undefined);
    },
    didInsertElement: function didInsertElement() {
      this._super.apply(this, arguments);
      this.$('#mailrepeat').on('paste', Ember.run.bind(this, this.get('pasteEvent')));
      this.$('#passrepeat').on('paste', Ember.run.bind(this, this.get('pasteEvent')));
    },
    willDestroyElement: function willDestroyElement() {
      this._super.apply(this, arguments);
      this.$('#mailrepeat').off('paste');
      this.$('#passrepeat').off('paste');
    },
    pasteEvent: function pasteEvent() {
      return false;
    }
  });
});
define('varwatch/components/row-link-to', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.LinkComponent.extend({
    tagName: 'tr'
  });
});
define('varwatch/components/to-elsewhere', ['exports', 'ember-elsewhere/components/to-elsewhere'], function (exports, _toElsewhere) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _toElsewhere.default;
    }
  });
});
define('varwatch/components/variant-line', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({
    tagName: 'tbody',
    //classNames: ['hoverbody'],
    classNameBindings: ['showDetails'],
    actions: {
      toggleDetails: function toggleDetails() {
        this.set('showDetails', !this.get('showDetails'));
        this.get('onShowDetailsToggled')(this.get('showDetails'));
      }
    },
    init: function init() {
      //this.set('showDetails', false);
      //console.log("variant initialized");
      this._super.apply(this, arguments);
      this.set('matchDbOrder', ['VarWatch', 'HGMD', 'ClinVar']);
      this.set('matchTypeOrder', ['perfect', 'codon', 'lof', 'gene']);
    },

    sortedMatches: Ember.computed('variant.varianthistory.[]', 'variant.varianthistory.@each.matchDatabase', 'variant.varianthistory.@each.matchType', 'variant.varianthistory.@each.matchHpoDist', function () {
      var _this = this;

      return this.get('variant.varianthistory').toArray().sort(function (a, b) {
        var amdb = a.get('matchDatabase');
        var bmdb = b.get('matchDatabase');
        var amt = a.get('matchType');
        var bmt = b.get('matchType');
        var ahd = a.get('matchHpoDist');
        var bhd = b.get('matchHpoDist');
        var ami = a.get('matchId');
        var bmi = b.get('matchId');

        var dbo = _this.get('matchDbOrder');
        var mto = _this.get('matchTypeOrder');

        if (amdb && bmdb) {
          if (amdb != bmdb) {
            return dbo.indexOf(amdb) - dbo.indexOf(bmdb);
          } else {
            if (amt && bmt) {
              if (amt != bmt) {
                return mto.indexOf(amt) - mto.indexOf(amt);
              } else {
                if (ahd && bhd) {
                  if (ahd != bhd) {
                    return bhd - ahd;
                  } else {
                    return bmi - ami;
                  }
                } else return bmi - ami;
              }
            } else return 0;
          }
        } else return 0;
      });
    })
  });
});
define('varwatch/components/warning-message', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Component.extend({});
});
define('varwatch/components/welcome-page', ['exports', 'ember-welcome-page/components/welcome-page'], function (exports, _welcomePage) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _welcomePage.default;
    }
  });
});
define('varwatch/components/x-option', ['exports', 'emberx-select/components/x-option'], function (exports, _xOption) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _xOption.default;
});
define('varwatch/components/x-select', ['exports', 'emberx-select/components/x-select'], function (exports, _xSelect) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _xSelect.default;
});
define('varwatch/controllers/application', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    session: Ember.inject.service('session'),
    actions: {
      invalidateSession: function invalidateSession() {
        this.get('session').invalidate();
      }
    }
  });
});
define('varwatch/controllers/convert', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    selectedReferenceAssembly: 'GRCh38',
    init: function init() {
      this._super.apply(this, arguments);
      this.set('okData', []);
      this.set('failData', []);
    },

    actions: {
      resetForm: function resetForm() {
        this.set('hgvsInput', '');
      },
      convertData: function convertData() {
        var hgvsTerms = this.get('hgvsInput').match(/\S+/g) || [];
        this.makeRequest(hgvsTerms, this.get('selectedReferenceAssembly') === 'GRCh37');
      },
      exportVcf: function exportVcf() {
        var vcf = "#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO";
        this.get('okData').forEach(function (itm) {
          var line = itm.chr + '\t' + itm.pos + '\t' + itm.input + '\t' + (itm.ref === '-' ? '.' : itm.ref) + '\t' + (itm.alt === '-' ? '.' : itm.alt) + '\t.\t.\t.';
          vcf += '\n';
          vcf += line;
        });
        //console.log(vcf);
        var blob = new Blob([vcf], { type: "text/plain;charset=utf-8" });
        saveAs(blob, "variants.vcf");
      },
      clearOkData: function clearOkData() {
        this.get('okData').clear();
      },
      clearFailData: function clearFailData() {
        this.get('failData').clear();
      }
    },
    makeRequest: function makeRequest(terms, useoldassembly) {
      if (terms.length === 0) return;
      var self = this;
      var term = terms.shift();
      if (!this.get('okData').findBy('input', term) && !this.get('failData').findBy('input', term)) {
        var endpointdomain = useoldassembly ? '/grch37ensembl' : 'https://rest.ensembl.org';
        var url = endpointdomain + "/vep/human/hgvs/" + term + "?content-type=application/json";
        Ember.$.getJSON(url).done(function (data) {
          var response = data[0];
          var refalt = response.allele_string.split('/');
          self.get('okData').pushObject({
            input: response.input,
            chr: response.seq_region_name,
            pos: response.start,
            ref: refalt[0],
            alt: refalt[1]
          });
        }).fail(function (a1, a2, a3) {
          if (a1.responseJSON && a1.responseJSON.error) self.get('failData').pushObject({
            input: term,
            errorString: a1.responseJSON.error
          });else if (!a3) self.get('failData').pushObject({
            input: term,
            errorString: "Variant could not be verified. The Ensembl rest service did not respond."
          });else self.get('failData').pushObject({
            input: term,
            errorString: "Variant could not be verified. The Ensembl rest service responded with the following error: " + a3 + "."
          });
        }).always(function () {
          self.makeRequest(terms, useoldassembly);
        });
      } else {
        self.makeRequest(terms, useoldassembly);
      }
    }
  });
});
define('varwatch/controllers/datasets/details', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    queryParams: ['details'],
    //details: [],
    actions: {
      rowClicked: function rowClicked() {
        //console.log("the row has been clicked!!!");
      },
      toggleStatusHistory: function toggleStatusHistory() {
        this.set('showStatusHistory', !this.get('showStatusHistory'));
      },
      toggleErrorVariants: function toggleErrorVariants() {
        this.set('showErrorVariants', !this.get('showErrorVariants'));
      },
      setParams: function setParams(p1, p2) {
        //console.log("params");
        //console.log(this.get("details"));
        var varid = parseInt(p1);
        var idx = this.get('details').indexOf(varid);
        if (p2) {
          if (idx === -1) {
            this.get('details').pushObject(parseInt(p1));
          }
        } else {
          if (idx !== -1) {
            this.get('details').removeAt(idx, 1);
          }
        }
      }
    },
    init: function init() {
      this.set('details', []);
      this.set('showStatusHistory', false);
      this.set('showErrorVariants', false);
      this._super.apply(this, arguments);
    }
  });
});
define('varwatch/controllers/datasets/index', ['exports', 'ember-concurrency'], function (exports, _emberConcurrency) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    processing: false,
    actions: {
      rowClicked: function rowClicked() {
        //console.log("the row has been clicked!!!");
      }
    },
    taskRefreshModel: (0, _emberConcurrency.task)( /*#__PURE__*/regeneratorRuntime.mark(function _callee() {
      var _this = this;

      var duration = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 0;

      var _loop;

      return regeneratorRuntime.wrap(function _callee$(_context2) {
        while (1) {
          switch (_context2.prev = _context2.next) {
            case 0:
              _loop = /*#__PURE__*/regeneratorRuntime.mark(function _loop() {
                var self, ads, tot;
                return regeneratorRuntime.wrap(function _loop$(_context) {
                  while (1) {
                    switch (_context.prev = _context.next) {
                      case 0:
                        _this.set('processing', false);
                        self = _this;
                        _context.next = 4;
                        return _this.store.findAll('dataset', { reload: true });

                      case 4:
                        ads = _context.sent;

                        ads.forEach(function (dataset) {
                          var status = dataset.get('status.status');
                          var id = dataset.get('status.id');
                          if (id === undefined) {
                            self.set('processing', true);
                          } else {
                            if (status === undefined || status === 'processing' || status === 'submitted') {
                              //console.log("must refresh status");
                              self.set('processing', true);
                              self.store.findRecord('status', id);
                            }
                          }
                        });
                        _this.set('model', ads);
                        tot = _this.get('processing') ? 10000 : 30000;
                        //console.log(tot+" ms")

                        _context.next = 10;
                        return (0, _emberConcurrency.timeout)(tot);

                      case 10:
                      case 'end':
                        return _context.stop();
                    }
                  }
                }, _loop, _this);
              });

            case 1:
              if (!true) {
                _context2.next = 5;
                break;
              }

              return _context2.delegateYield(_loop(), 't0', 3);

            case 3:
              _context2.next = 1;
              break;

            case 5:
            case 'end':
              return _context2.stop();
          }
        }
      }, _callee, this);
    })),
    sortedDatasets: Ember.computed('model', 'model.@each.statusDatetime', function () {
      return this.get('model').toArray().sort(function (a, b) {
        return a.get('statusDatetime') - b.get('statusDatetime');
      });
    })
  });
});
define('varwatch/controllers/help', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({});
});
define('varwatch/controllers/index', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    session: Ember.inject.service(),
    currentlyLoading: true,
    promiseCount: Ember.computed('promises', function () {
      return this.get('promises.length');
    }),
    finishedPromises: Ember.computed('_finishedPromises', function () {
      return this.get('_finishedPromises');
    }),
    _finishedPromises: 0,
    init: function init() {
      this._super('arguments');
      this.set('promises', []);
    },

    actions: {
      clearNewMatches: function clearNewMatches() {
        //console.log("clear Matches");
        //console.log(this.get('target'));
        //console.log(this.get('target.router'));
        //console.log(this.get('target.target.router'));
        //console.log(this.get('target.target'));
        //this.get('target.router').refresh();
        var headers = {};
        this.get('session').authorize('authorizer:varwatchoauth2-bearer', function (headerName, headerValue) {
          headers[headerName] = headerValue;
        });
        var self = this;
        Ember.$.ajax({
          url: "/varwatch/api/matching/ack",
          type: "PUT",
          headers: headers,
          processData: false,
          contentType: "application/json; charset=UTF-8",
          error: function error(a1, a2, a3) {
            /*console.log("ajax error");
            console.log(a1);
            console.log(a2);
            console.log(a3);*/
          },
          success: function success(a1, a2, a3) {
            /*console.log("ajax success");
            console.log(a1);
            console.log(a2);
            console.log(a3);*/
            self.get('store').unloadAll('match');
            //console.log("before refresh")
            self.get('target.router').refresh();
            //console.log("after refresh")
          }
        });
      }
    },
    sortedMatches: Ember.computed('model', 'model.@each.statusDatetime', function () {
      return this.get('model').toArray().sort(function (a, b) {
        var datediff = b.get('statusDatetime') - a.get('statusDatetime');
        if (datediff !== 0) return datediff;else return b.get('id') - a.get('id');
      });
    }),
    loadModel: function loadModel() {
      var _this = this;

      if (!this.get('model')) {
        this.set('currentlyLoading', true);
      }
      this.set('matchCount', 0);
      this.get('promises').clear();
      this.set('_finishedPromises', 0);
      this.store.findAll('match', { reload: true }).then(function (allmatches) {
        var filteredmatches = allmatches.filter(function (match) {
          return match.get('acknowledged') === false;
        });
        var datasets = new Set();
        var variants = new Set();
        var status = new Set();
        filteredmatches.forEach(function (match) {
          variants.add(match.get('queryVariantId'));
          datasets.add(match.get('datasetId'));
          status.add(match.get('statusId'));
        });
        _this.set('promiseCount', variants.size + datasets.size + status.size);
        _this.set('matchCount', filteredmatches.length);
        variants.forEach(function (id) {
          _this.get('promises').pushObject(_this.store.findRecord('variant', id, { backgroundReload: false }).then(function (value) {
            _this.incrementFinishedPromises();return value;
          }));
        });
        status.forEach(function (id) {
          _this.get('promises').pushObject(_this.store.findRecord('variantstatus', id, { backgroundReload: false }).then(function (value) {
            _this.incrementFinishedPromises();return value;
          }));
        });
        datasets.forEach(function (id) {
          _this.get('promises').pushObject(_this.store.findRecord('dataset', id, { backgroundReload: false }).then(function (value) {
            _this.incrementFinishedPromises();return value;
          }));
        });

        Ember.RSVP.all(_this.get('promises')).then(function () {
          _this.set('model', filteredmatches);
          _this.set('currentlyLoading', false);
        });
      });
    },
    incrementFinishedPromises: function incrementFinishedPromises() {
      this.set('_finishedPromises', this.get('_finishedPromises') + 1);
    }
  });
});
define('varwatch/controllers/login', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    session: Ember.inject.service('session'),

    actions: {
      authenticate: function authenticate() {
        var _this = this;

        var _getProperties = this.getProperties('identification', 'password'),
            identification = _getProperties.identification,
            password = _getProperties.password;

        this.get('session').authenticate('authenticator:varwatchoauth2', identification, password).catch(function (reason) {
          //console.log("error");
          //console.log(reason);
          var errormessage = "Internal Server Error";
          if (reason && reason.description) {
            switch (reason.description) {
              case "Missing parameters: username password":
              case "Missing parameters: username":
              case "Missing parameters: password":
                errormessage = "Please provide both eMail and Password";
                break;
              case "User or Password not valid":
                errormessage = "eMail or Password incorrect";
                break;
              default:
            }
          }
          _this.set('errorMessage', errormessage);
        });
      },
      doNotShowModal: function doNotShowModal() {
        //console.log("killing modal")
        this.set('errorMessage', "");
        //this.set('showModal', false);
      }
    },
    doShowModal: function doShowModal() {
      //console.log("showing modal");
      //this.set('showModal', true);
    },
    init: function init() {
      this.set("identification", "");
      this.set("password", "");
      this._super.apply(this, arguments);
      //this.set('showModal', false);
    }
  });
});
define('varwatch/controllers/oauth', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    session: Ember.inject.service(),
    queryParams: [{ redirectUri: 'redirect_uri' }, { clientId: 'client_id' }, { responseType: 'response_type' }, { state: 'state' }],
    redirectUri: null,
    clientId: null,
    responseType: null,
    state: null,
    redirectUriMalformed: Ember.computed('redirectUri', function () {
      var rdu = this.get('redirectUri');
      return !(rdu && (rdu.startsWith('http://') || rdu.startsWith('https://')));
    }),
    checkParams: function checkParams() {
      if (this.get('clientId')) {
        var self = this;
        var params = "?client_id=" + this.get('clientId');
        Ember.$.ajax({
          url: "/varwatch/api/client/name" + params,
          type: "GET",
          error: function error(a1, a2, a3) {
            /*console.log("ajax error");
            console.log(a1);
            console.log(a2);
            console.log(a3);*/
            if (a1.responseText && JSON.parse(a1.responseText).description == "Client not valid") {
              self.setParamError('clientidinvalid');
            } else {
              self.setParamError('servererror');
            }
          },
          success: function success(a1, a2, a3) {
            /*console.log("ajax success!");
            console.log(a1);
            console.log(a2);
            console.log(a3);*/
            var response = JSON.parse(a1);
            var clientname = response.client_name;
            //console.log(clientname);
            self.set("clientName", clientname);
            var rdu = self.get('redirectUri');
            if (rdu) {
              params += "&redirect_uri=" + rdu;
              Ember.$.ajax({
                url: "/varwatch/api/client/uri/check" + params,
                type: "GET",
                error: function error(a1, a2, a3) {
                  /*console.log("ajax error");
                  console.log(a1);
                  console.log(a2);
                  console.log(a3);*/
                  self.setParamError('redirecturiinvalid');
                },
                success: function success(a1, a2, a3) {
                  /*console.log("ajax success!");
                  console.log(a1);
                  console.log(a2);
                  console.log(a3);*/
                  var restype = self.get('responseType');
                  if (!restype) {
                    window.location.assign(self.get('redirectUri') + '#error=invalid_request' + (self.get('state') !== null ? '&state=' + self.get('state') : ""));
                  } else if (restype != 'token') {
                    window.location.assign(self.get('redirectUri') + '#error=unsupported_response_type' + (self.get('state') !== null ? '&state=' + self.get('state') : ""));
                  } else {
                    self.set('paramsOk', true);
                  }
                }
              });
            } else {
              self.setParamError('redirecturimissing');
            }
          }
        });
      } else {
        this.setParamError('clientidmissing');
      }
    },

    actions: {
      checkTarget: function checkTarget() {
        // console.log(this.get('state'));
        // console.log(this.get('target.router'));
        // console.log(this.get('target.target.router'));
        // console.log(this.get('target.target'));
      },
      authenticate: function authenticate() {
        var self = this;

        var _getProperties = this.getProperties('identification', 'password'),
            identification = _getProperties.identification,
            password = _getProperties.password;

        var params = '?grant_type=password&username=' + identification + '&password=' + password + '&client_id=' + this.get('clientId') + '&client_secret=secret';
        Ember.$.ajax({
          url: "/varwatch/api/registration/token" + params,
          type: "POST",
          processData: false,
          contentType: "application/x-www-form-urlencoded",
          error: function error(a1, a2, a3) {
            /*console.log("ajax error");
            console.log(a1);
            console.log(a2);
            console.log(a3);*/
            var errormessage = "Internal Server Error";
            if (a1.responseJSON) {
              if (a1.responseJSON.description) {
                var err = a1.responseJSON.description;
                switch (err) {
                  case "Missing parameters: username password":
                  case "Missing parameters: username":
                  case "Missing parameters: password":
                    errormessage = "Please provide both eMail and Password";
                    break;
                  case "User or Password not valid":
                    errormessage = "eMail or Password incorrect";
                    break;
                  case "Client not valid":
                    errormessage = 'ClientId "' + self.get('clientId') + '" is invalid';
                    break;
                  default:
                }
              }
            }
            self.set('errorMessage', errormessage);
          },
          success: function success(a1, a2, a3) {
            /*console.log("ajax success");
            console.log(a1);
            console.log(a2);
            console.log(a3);*/
            self.set("authenticationOk", true);
            self.set("token", a1.access_token);
            self.set("expiresIn", a1.expires_in);
          }
        });
      },
      allow: function allow() {
        var self = this;
        var headers = {
          Authorization: "Bearer " + this.get("token")
        };
        Ember.$.ajax({
          url: "/varwatch/api/registration/token/validation",
          type: "GET",
          processData: false,
          headers: headers,
          contentType: "application/x-www-form-urlencoded",
          error: function error(a1, a2, a3) {
            /*console.log("ajax error");
            console.log(a1);
            console.log(a2);
            cosole.log(a3);*/
            self.set('errorMessage', "Authentication expired! Please authenticate again.");
            self.set("authenticationOk", false);
            self.set("token", null);
            self.set("expiresIn", null);
            self.set("password", "");
          },
          success: function success(a1, a2, a3) {
            /*console.log("ajax success");
            console.log(a1);
            console.log(a2);
            console.log(a3);*/
            //console.log("token good");
            self.redirectWithToken();
          }
        });
        //window.location.assign(`${this.get('redirectUri')}#access_token=${this.get('token')}&token_type=bearer&expires_in=${this.get('expiresIn')}${this.get('state')!==null?`&state=${this.get('state')}`:""}`);
      },
      deny: function deny() {
        //console.log("deny")
        window.location.assign(this.get('redirectUri') + '#error=access_denied' + (this.get('state') !== null ? '&state=' + this.get('state') : ""));
      },
      doNotShowModal: function doNotShowModal() {
        this.set('errorMessage', "");
      }
    },
    init: function init() {
      this.set("identification", "");
      this.set("password", "");
      this._super.apply(this, arguments);
    },
    redirectWithToken: function redirectWithToken() {
      window.location.assign(this.get('redirectUri') + '#access_token=' + this.get('token') + '&token_type=bearer&expires_in=' + this.get('expiresIn') + (this.get('state') !== null ? '&state=' + this.get('state') : ""));
    },
    setParamError: function setParamError(error) {
      var textmiddle = " would like to gain access to your VarWatch account, but unfortunately ";
      var textend = " was provided.";
      switch (error) {
        case 'clientidmissing':
          this.set('paramError', "A client application" + textmiddle + "<strong>no client ID</strong>" + textend);
          break;
        case 'clientidinvalid':
          this.set('paramError', "A client application" + textmiddle + "an <strong>invalid client ID</strong>" + textend);
          break;
        case 'servererror':
          this.set('paramError', "A client application" + textmiddle + "the client ID could not be verified. Please try again later.");
          break;
        case 'redirecturimissing':
          this.set('paramError', "The client application " + this.get('clientName') + textmiddle + "<strong>no redirect URI</strong>" + textend);
          break;
        case 'redirecturiinvalid':
          this.set('paramError', "The client application " + this.get('clientName') + textmiddle + "an <strong>invalid redirect URI</strong>" + textend);
          break;
      }
    }
  });
});
define('varwatch/controllers/recover', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    actions: {
      requestPw: function requestPw() {
        if (this.get('emailNotOk')) {
          this.set('showErrors', true);
        } else {
          this.set('showErrors', false);
          var self = this;
          var mailaddy = this.get("mail").trim();
          var data = {
            mail: mailaddy
          };
          var datastring = JSON.stringify(data);
          //console.log(data);
          Ember.$.ajax({
            url: "/varwatch/api/registration/password/reset",
            type: "POST",
            data: datastring,
            processData: false,
            contentType: "application/json; charset=UTF-8",
            error: function error(a1, a2, a3) {
              if (a1 && a1.status === 406 && a1.responseJSON && a1.responseJSON.description === "Cant find user") {
                self.set('errorMessage', 'A user registered to the eMail "' + mailaddy + '" could not be found.');
              } else {
                //console.log("ajax error");
                //console.log(a1);
                //console.log(a2);
                //console.log(a3);
                self.set('errorMessage', 'Error setting new password for "' + mailaddy + '".');
              }
            },
            success: function success(a1, a2, a3) {
              //console.log("ajax success");
              //console.log(a1);
              //console.log(a2);
              //console.log(a3);
              self.set('successMessage', 'A new password has been sent to "' + mailaddy + '".');
            }
          });
        }
      },
      closeModalDialogSuccess: function closeModalDialogSuccess() {
        this.set('successMessage', "");
        this.transitionToRoute('login');
      },
      closeModalDialogError: function closeModalDialogError() {
        this.set('errorMessage', "");
      }
    },
    emailNotOk: Ember.computed('mail', function () {
      return !/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(this.get('mail').trim());
    }),
    init: function init() {
      this.set("mail", "");
      this.set('successMessage', "");
      this.set('errorMessage', "");
      this.set('showErrors', false);
      this._super.apply(this, arguments);
    }
  });
});
define('varwatch/controllers/redirect', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    queryParams: ['error', 'state']
  });
});
define('varwatch/controllers/submit', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    session: Ember.inject.service(),
    hpo: Ember.inject.service(),
    actions: {
      hpoPurgeExpand: function hpoPurgeExpand() {
        var _this = this;

        var parseresult = this.get('hpo').parseStringForPhenotypes(this.get('hpoterms'));
        var terms = parseresult.ok;
        var newterms = "";

        terms.forEach(function (term) {
          var hpo = _this.get('hpo').getHpo(term);
          if (hpo.isAltOf) {
            hpo = _this.get('hpo').getHpo(hpo.isAltOf);
          }
          var t = hpo.term + " " + hpo.desc;
          if (newterms) {
            newterms += ", ";
          }
          newterms += t;
        });
        this.set('warningMessage', this.buildWarningMessage(parseresult.replaced, parseresult.failed));
        this.set('hpoterms', newterms);
      },
      hpoPurgeCollapse: function hpoPurgeCollapse() {
        var parseresult = this.get('hpo').parseStringForPhenotypes(this.get('hpoterms'));
        var terms = parseresult.ok;
        var newterms = "";

        terms.forEach(function (term) {
          if (newterms) {
            newterms += ", ";
          }
          newterms += term;
        });
        this.set('warningMessage', this.buildWarningMessage(parseresult.replaced, parseresult.failed));
        this.set('hpoterms', newterms);
      },
      clearHpoTerms: function clearHpoTerms() {
        this.set('hpoterms', "");
      },
      addHpo: function addHpo(hpo) {
        var terms = this.get('hpoterms').trim();
        if (terms.length > 0) {
          if (terms.slice(-1) === ',') {
            terms += ' ';
          } else {
            terms += ', ';
          }
        }
        var newterms = terms.concat(hpo);
        this.set('hpoterms', newterms);
      },
      addHgvsVariants: function addHgvsVariants() {
        var splithgvsterms = this.get('hgvsinput').trim().split(/\s+/);
        var useoldassembly = this.get('selectedReferenceAssembly') === 'GRCh37';
        this.checkHgvsTerms(splithgvsterms, useoldassembly);
        this.set('hgvsinput', '');
      },
      addDirectVariant: function addDirectVariant() {
        if (this.get('positionNotOk') || this.get('refNotOk') || this.get('altNotOk') || this.get('refAndAltEmpty')) {
          this.set('showVariantErrors', true);
        } else {
          var variant = {
            variant: {
              referenceName: this.get('chrom'),
              start: this.get('position'),
              referenceBases: this.get('refbase').toUpperCase(),
              alternateBases: this.get('altbase').toUpperCase()
            }
          };
          this.get('directVariants').pushObject(variant);
          this.set('showVariantErrors', false);
        }
      },
      removeHgvsVariant: function removeHgvsVariant(index) {
        this.get('hgvsVariants').replace(index, 1);
      },
      removeDirectVariant: function removeDirectVariant(index) {
        this.get('directVariants').replace(index, 1);
      },
      submitData: function submitData() {
        this.set('showVariantErrors', false);
        if (this.get('descriptionNotOk') || this.get('hpoTermsNotOk')) {
          this.set('showErrors', true);
          Ember.run.schedule('afterRender', function () {
            var errorElements = document.getElementsByClassName('has-error');
            if (errorElements.length > 0) {
              errorElements[0].scrollIntoView();
            }
          });
        } else {
          var payload = {
            patient: {
              description: this.get('description'),
              features: this.getFeatures(),
              inheritanceMode: this.get('inheritance'),
              ageOfOnset: this.get('onset'),
              assembly: this.get('selectedReferenceAssembly')
            }
          };
          var inputtype = this.get('selectedVariantInput');
          switch (inputtype) {
            case 'vcf':
              this.submitVcfData(payload);
              break;
            case 'hgvs':
              this.submitHgvsData(payload);
              break;
            case 'direct':
              this.submitDirectData(payload);
              break;
            default:
              console.log('unknown data type');
          }
        }
      },
      updateVcfFile: function updateVcfFile(file) {
        this.set('vcffile', file);
      },
      closeModalDialog: function closeModalDialog() {
        this.set('errorMessage', "");
        this.set('successMessage', "");
        this.set('warningMessage', "");
      },
      resetVariantInput: function resetVariantInput() {
        this.set('refbase', "");
        this.set('altbase', "");
        this.set('position', "");
        this.set('chrom', 1);
        this.set('showVariantErrors', false);
      },
      resetForm: function resetForm() {
        this.set('description', "");
        this.set('hpoterms', '');
        this.set('hgvsinput', '');
        this.set('hgvsVariants', []);
        this.set('directVariants', []);
        this.set('vcffile', undefined);
        this.send('resetVariantInput');
        this.set('showErrors', false);
        this.set('onset', '');
        this.set('inheritance', '');
      },
      didMakeSelection: function didMakeSelection(which, value) {
        //console.log(which)
        //console.log(value)
        //console.log(evt)
        this.set(which, value);
      }
    },
    init: function init() {
      this._super.apply(this, arguments);
      this.set('description', "");
      this.set('hpoterms', '');
      this.set('hgvsinput', '');
      this.set('hgvsVariants', []);
      this.set('directVariants', []);
      this.set('selectedReferenceAssembly', 'GRCh38');
      this.set('selectedVariantInput', 'direct');
      this.set('vcffile', undefined);
      this.set('refbase', "");
      this.set('altbase', "");
      this.set('position', "");

      var chromosomes = [];
      for (var i = 1; i < 23; i++) {
        chromosomes.push('' + i);
      }
      chromosomes.push('X');
      chromosomes.push('Y');
      this.set('chromosomes', chromosomes);
      this.set('showVariantErrors', false);
      this.set('showErrors', false);
    },

    chrom: 1,
    inheritance: "",
    onset: "",
    positionNotOk: Ember.computed('position', function () {
      var re = new RegExp("^[0-9]+$", "i");
      return !re.test(this.get('position'));
    }),
    refNotOk: Ember.computed('refbase', function () {
      var re = new RegExp("^([acgt]*|[acgt]+(,[acgt]+)*)$", "i");
      return !re.test(this.get('refbase'));
    }),
    altNotOk: Ember.computed('altbase', function () {
      var re = new RegExp("^([acgt]*|[acgt]+(,[acgt]+)*)$", "i");
      return !re.test(this.get('altbase'));
    }),
    refAndAltEmpty: Ember.computed('altbase', 'refbase', function () {
      return this.get('altbase') === "" && this.get('refbase') === "";
    }),
    descriptionNotOk: Ember.computed('description', function () {
      return this.get('description') === "";
    }),
    hpoTermsNotOk: Ember.computed('hpoterms', function () {
      return this.getFeatures().length === 0;
    }),
    directVariantsEmpty: Ember.computed('directVariants.length', function () {
      return this.get('directVariants.length') === 0;
    }),
    hgvsVariantsEmpty: Ember.computed('hgvsVariants.length', function () {
      return this.get('hgvsVariants.length') === 0;
    }),
    noFileSelected: Ember.computed('vcffile', function () {
      return this.get('vcffile') === undefined;
    }),
    buildWarningMessage: function buildWarningMessage(replaced, failed) {
      var _this2 = this;

      var warning = "";
      if (replaced.length > 0) {
        warning += "<strong>Alternative Terms that have been replaced:</strong>";
        replaced.forEach(function (term) {
          var alt = _this2.get('hpo').getHpo(term).isAltOf;
          warning += "<br>" + term + " -> " + _this2.get('hpo').getHpo(term).isAltOf + ": " + _this2.get('hpo').getHpo(alt).desc;
        });
      }
      if (failed.length > 0) {
        if (warning) warning += "<br>";
        warning += "<strong>Terms not representing a phenotype that have been discarded:</strong>";
        failed.forEach(function (term) {
          warning += "<br>" + term;
          var hpo = _this2.get('hpo').getHpo(term);
          if (hpo) {
            if (hpo.isAltOf) {
              warning += ": is alternative Term of " + hpo.isAltOf + ": " + _this2.get('hpo').getHpo(hpo.isAltOf).desc;
            } else {
              warning += ": " + hpo.desc;
            }
          } else warning += ": not found";
        });
      }
      return warning;
    },
    getFeatures: function getFeatures() {
      var feats = [];
      var terms = this.get('hpo').parseStringForPhenotypes(this.get('hpoterms')).ok;
      terms.forEach(function (term) {
        feats.push({ id: term });
      });
      return feats;
    },
    submitHgvsData: function submitHgvsData(payload) {
      if (this.get('hgvsVariantsEmpty')) {
        this.set('showErrors', true);
        Ember.run.schedule('afterRender', function () {
          var errorElements = document.getElementsByClassName('has-error');
          if (errorElements.length > 0) {
            errorElements[0].scrollIntoView();
          }
        });
      } else {
        var gf = [];
        this.get('hgvsVariants').forEach(function (variant) {
          return gf.push({ variant: { hgvs: variant.hgvs } });
        });
        payload.patient.genomicFeatures = gf;
        this.sendData(payload, "hgvs");
      }
    },
    submitDirectData: function submitDirectData(payload) {
      if (this.get('directVariantsEmpty')) {
        this.set('showErrors', true);
        Ember.run.schedule('afterRender', function () {
          var errorElements = document.getElementsByClassName('has-error');
          if (errorElements.length > 0) {
            errorElements[0].scrollIntoView();
          }
        });
      } else {
        payload.patient.genomicFeatures = this.get('directVariants');
        //console.log(payload);
        //console.log(JSON.stringify(payload));
        this.sendData(payload, "variant");
      }
    },
    submitVcfData: function submitVcfData(payload) {
      if (this.get('noFileSelected')) {
        this.set('showErrors', true);
        Ember.run.schedule('afterRender', function () {
          var errorElements = document.getElementsByClassName('has-error');
          if (errorElements.length > 0) {
            errorElements[0].scrollIntoView();
          }
        });
      } else {
        //payload.patient.vcfFileAssembly = this.get('selectedReferenceAssembly');
        //console.log(this.get('vcffile'));
        var reader = new FileReader();
        var self = this;
        reader.onload = function () {
          var res = reader.result;
          var pos = res.indexOf(',');
          var base64res = res.substring(pos + 1);
          payload.patient.vcfFile = base64res;
          self.sendData(payload, 'vcf');
        };
        reader.onerror = function () {
          self.set('errorMessage', "File \"" + self.get('vcffile').name + "\" could not be read.");
        };
        reader.readAsDataURL(this.get('vcffile'));
      }
    },
    sendData: function sendData(payload, path) {
      var data = JSON.stringify(payload);
      var headers = {};
      this.get('session').authorize('authorizer:varwatchoauth2-bearer', function (headerName, headerValue) {
        headers[headerName] = headerValue;
      });
      var self = this;
      Ember.$.ajax({
        url: "/varwatch/api/submit/" + path,
        type: "POST",
        data: data,
        headers: headers,
        processData: false,
        contentType: "application/json; charset=UTF-8",
        error: function error(a1, a2, a3) {
          //console.log("ajax error");
          console.log(a1);
          console.log(a2);
          console.log(a3);
          if (a1.responseText && a1.status && a1.status === 406) {
            var obj = JSON.parse(a1.responseText);
            if (obj.message) self.set('errorMessage', "Data rejected: " + obj.description);else self.set('errorMessage', "Data rejected: " + a1.responseText);
          } else if (a1.status && a1.status === 500) {
            self.set('errorMessage', "Internal server error");
          } else {
            self.set('errorMessage', "Upload failed");
          }
          self.set('showErrors', false);
        },
        success: function success(a1, a2, a3) {
          //console.log("ajax success");
          //console.log(a1);
          //console.log(a2);
          //console.log(a3);
          var successmessage = "New dataset \"" + self.get('description') + "\" created.";
          var parseresult = self.get('hpo').parseStringForPhenotypes(self.get('hpoterms'));
          var warningmessage = self.buildWarningMessage(parseresult.replaced, parseresult.failed);
          if (warningmessage.length > 0) successmessage += "<br><br>" + warningmessage;
          self.set('successMessage', successmessage);
          self.send('resetForm');
          self.set('showErrors', false);
        }
      });
    },
    checkHgvsTerms: function checkHgvsTerms(terms, useoldassembly) {
      if (terms.length === 0) {
        Ember.run.schedule('afterRender', this, this.setTooltips);
        return;
      }
      var self = this;
      var term = terms.shift();
      var endpointdomain = useoldassembly ? '/grch37ensembl' : 'https://rest.ensembl.org';
      var url = endpointdomain + "/vep/human/hgvs/" + term + "?content-type=application/json";
      Ember.$.getJSON(url).done(function (data) {
        self.get('hgvsVariants').pushObject({
          hgvs: term
        });
      }).fail(function (a1, a2, a3) {
        //console.log(a1);
        //console.log(a2);
        //console.log(a3);
        if (a1.responseJSON && a1.responseJSON.error) self.get('hgvsVariants').pushObject({
          hgvs: term,
          errorString: a1.responseJSON.error
        });else if (!a3) self.get('hgvsVariants').pushObject({
          hgvs: term,
          errorString: "Variant could not be verified. The Ensembl rest service did not respond."
        });else self.get('hgvsVariants').pushObject({
          hgvs: term,
          errorString: "Variant could not be verified. The Ensembl rest service responded with the following error: " + a3 + "."
        });
      }).always(function () {
        //console.log('next round');
        self.checkHgvsTerms(terms, useoldassembly);
      });
    },
    setTooltips: function setTooltips() {
      Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
    }
  });
});
define('varwatch/controllers/testdata', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    init: function init() {
      this._super.apply(this, arguments);
      var testdata = [];
      var testdata2 = [];
      for (var i = 1; i <= 20; i++) {
        testdata.pushObject({ vcf: 'usecase_' + i + '.vcf', hpo: 'hpo_' + i + '.txt' });
      }
      for (var _i = 21; _i <= 25; _i++) {
        testdata2.pushObject('usecase_' + _i + '.xlsx');
      }
      this.set('testData', testdata);
      this.set('testData2', testdata2);
    }
  });
});
define('varwatch/controllers/userinfo', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Controller.extend({
    session: Ember.inject.service(),
    actions: {
      editData: function editData() {
        this.set('editUserInfo', true);
        //this.set('backupModel', this.get('model'));
      },
      cancelEdit: function cancelEdit() {
        this.set('editUserInfo', false);
        this.get('model').rollbackAttributes();
        this.set('showFormErrors', false);
      },
      saveChanges: function saveChanges() {
        //console.log(this.get('model').save());
        var self = this;
        if (this.get('firstNameNotOk') || this.get('lastNameNotOk') || this.get('institutionNotOk') || this.get('addressNotOk') || this.get('cityNotOk') || this.get('postalCodeNotOk') || this.get('countryNotOk')) {
          this.set('showFormErrors', true);
          /*Ember.run.schedule('afterRender', () => { 
            let errorElements = document.getElementsByClassName('has-error');
            if (errorElements.length>0) {errorElements[0].scrollIntoView();}
          });*/
        } else {
          this.get('model').save().then(function () {
            self.set('editUserInfo', false);
            self.set('showFormErrors', false);
            self.set("successMessageTitle", "Changes applied!");
            self.set("successMessage", "The changes to your personal information have been applied successfully.");
          }, function () {
            self.set('showFormErrors', false);
            self.set("errorMessageTitle", "Error saving changes!");
            self.set("errorMessage", "Something went wrong.");
          });
        }
      },
      changePassword: function changePassword() {
        if (this.get('oldPasswordNotOk') || this.get('passwordNotOk') || this.get('passrepeatNotOk')) {
          this.set('showErrors', true);
        } else {
          this.set('showErrors', false);
          var self = this;
          var headers = {};
          this.get('session').authorize('authorizer:varwatchoauth2-bearer', function (headerName, headerValue) {
            headers[headerName] = headerValue;
          });
          var dataobject = {
            password: this.get('password'),
            oldPassword: this.get('oldpassword')
          };
          //console.log("pwchange!");
          //let props = this.getProperties('password');
          //console.log(props);
          var data = JSON.stringify(dataobject);
          //console.log(data);
          Ember.$.ajax({
            url: "/varwatch/api/registration/password/new",
            type: "POST",
            data: data,
            headers: headers,
            processData: false,
            contentType: "application/json; charset=UTF-8",
            error: function error(a1, a2, a3) {
              //console.log("error");
              //console.log(a1);
              //console.log(a2);
              //console.log(a3);
              if (a1.responseJSON && a1.responseJSON.description && a1.responseJSON.description === "User or Password not valid") {
                self.set("errorMessage", "The current Password you provided is incorrect.");
                self.set("oldpassword", "");
                self.set("showErrors", true);
              } else {
                self.set("errorMessageTitle", "Error setting new password!");
                self.set("errorMessage", "Something went wrong.");
              }
            },
            success: function success(a1, a2, a3) {
              //console.log("success");
              //console.log(a1);
              //console.log(a2);
              //console.log(a3);
              self.set("successMessageTitle", "New password set!");
              self.set("successMessage", "The password change has been applied successfully.");
            }
          });
        }
      },
      setReportingInterval: function setReportingInterval() {
        var self = this;
        var headers = {};
        var shed = this.get("shed");
        this.get('session').authorize('authorizer:varwatchoauth2-bearer', function (headerName, headerValue) {
          headers[headerName] = headerValue;
        });
        //console.log("setting shedule to "+shed);
        Ember.$.ajax({
          url: "/varwatch/api/user/report?schedule=" + shed,
          type: "POST",
          headers: headers,
          processData: false,
          contentType: "application/json; charset=UTF-8",
          error: function error(a1, a2, a3) {
            //console.log("error");
            //console.log(a1);
            //console.log(a2);
            //console.log(a3);
            self.set("errorMessageTitle", "Error setting new report schedule!");
            self.set("errorMessage", "Something went wrong.");
          },
          success: function success(a1, a2, a3) {
            //console.log("success");
            //console.log(a1);
            //console.log(a2);
            //console.log(a3);
            self.set("successMessageTitle", "Report schedule changed!");
            self.set("successMessage", "Report schedule has been sucessfully set to " + shed + ".");
          }
        });
      },
      didMakeSelection: function didMakeSelection(which, value) {
        this.set("shed", value);
      },
      closeModalDialogSuccess: function closeModalDialogSuccess() {
        this.set('successMessage', "");
        this.set('successMessageTitle', "");
        this.clearForm();
      },
      closeModalDialogError: function closeModalDialogError() {
        this.set('errorMessage', "");
        this.set('errorMessageTitle', "");
      }
    },
    oldPasswordNotOk: Ember.computed('oldpassword', function () {
      return this.get('oldpassword') === '';
    }),
    passwordNotOk: Ember.computed('password', function () {
      return this.get('password') === '';
    }),
    passrepeatNotOk: Ember.computed('passrepeat', 'password', function () {
      return this.get('passrepeat') !== this.get('password');
    }),
    shed: "never",
    editUserInfo: false,
    init: function init() {
      this.set('successMessage', "");
      this.set('errorMessage', "");
      this.set("oldpassword", "");
      this.set("password", "");
      this.set("passrepeat", "");
      this.set('showErrors', false);
      this._super.apply(this, arguments);
    },
    clearForm: function clearForm() {
      this.set("oldpassword", "");
      this.set("password", "");
      this.set("passrepeat", "");
    },

    showFormErrors: false,
    firstNameNotOk: Ember.computed('model.firstName', function () {
      return !this.get('model.firstName') || this.get('model.firstName').trim() === '';
    }),
    lastNameNotOk: Ember.computed('model.lastName', function () {
      return !this.get('model.lastName') || this.get('model.lastName').trim() === '';
    }),
    institutionNotOk: Ember.computed('model.institution', function () {
      return !this.get('model.institution') || this.get('model.institution').trim() === '';
    }),
    addressNotOk: Ember.computed('model.address', function () {
      return !this.get('model.address') || this.get('model.address').trim() === '';
    }),
    postalCodeNotOk: Ember.computed('model.postalCode', function () {
      return !this.get('model.postalCode') || this.get('model.postalCode').trim() === '';
    }),
    cityNotOk: Ember.computed('model.city', function () {
      return !this.get('model.city') || this.get('model.city').trim() === '';
    }),
    countryNotOk: Ember.computed('model.country', function () {
      return !this.get('model.country') || this.get('model.country').trim() === '';
    })
  });
});
define('varwatch/helpers/add', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var add = function add(params) {
    return params.reduce(function (acc, val) {
      return acc + val;
    });
  };

  exports.default = Ember.Helper.helper(add);
});
define('varwatch/helpers/and', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var and = function and(params) {
    return params[0] && params[1];
  };

  exports.default = Ember.Helper.helper(and);
});
define('varwatch/helpers/app-version', ['exports', 'varwatch/config/environment', 'ember-cli-app-version/utils/regexp'], function (exports, _environment, _regexp) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.appVersion = appVersion;
  var version = _environment.default.APP.version;
  function appVersion(_) {
    var hash = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

    if (hash.hideSha) {
      return version.match(_regexp.versionRegExp)[0];
    }

    if (hash.hideVersion) {
      return version.match(_regexp.shaRegExp)[0];
    }

    return version;
  }

  exports.default = Ember.Helper.helper(appVersion);
});
define('varwatch/helpers/array-contains', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.arrayContains = arrayContains;
  function arrayContains(params /*, hash*/) {
    var arr = params[0];
    var val = parseInt(params[1]);
    return arr.indexOf(val) !== -1;
  }

  exports.default = Ember.Helper.helper(arrayContains);
});
define('varwatch/helpers/cancel-all', ['exports', 'ember-concurrency/-helpers'], function (exports, _helpers) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.cancelHelper = cancelHelper;


  var CANCEL_REASON = "the 'cancel-all' template helper was invoked";

  function cancelHelper(args) {
    var cancelable = args[0];
    if (!cancelable || typeof cancelable.cancelAll !== 'function') {
      Ember.assert('The first argument passed to the `cancel-all` helper should be a Task or TaskGroup (without quotes); you passed ' + cancelable, false);
    }

    return (0, _helpers.taskHelperClosure)('cancel-all', 'cancelAll', [cancelable, CANCEL_REASON]);
  }

  exports.default = Ember.Helper.helper(cancelHelper);
});
define('varwatch/helpers/eq', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var eq = function eq(params) {
    return params[0] === params[1];
  };

  exports.default = Ember.Helper.helper(eq);
});
define('varwatch/helpers/gen-array-for-param', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.genArrayForHelper = genArrayForHelper;
  function genArrayForHelper(params /*, hash*/) {
    var id = parseInt(params[0]);
    var arr = [];
    arr.push(id);
    return arr;
  }

  exports.default = Ember.Helper.helper(genArrayForHelper);
});
define("varwatch/helpers/gen-family-link", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.genFamilyLink = genFamilyLink;
  function genFamilyLink(params /*, hash*/) {
    var family = params[0];
    return "http://www.ensembl.org/Multi/Family/Details?fm=" + family;
  }

  exports.default = Ember.Helper.helper(genFamilyLink);
});
define("varwatch/helpers/gen-gene-link", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.genGeneLink = genGeneLink;
  function genGeneLink(params /*, hash*/) {
    var gene = params[0];
    return "http://www.ensembl.org/Homo_sapiens/Gene/Summary?db=core;g=" + gene;
  }

  exports.default = Ember.Helper.helper(genGeneLink);
});
define("varwatch/helpers/gen-hpo-link", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.genHpoLink = genHpoLink;
  function genHpoLink(params /*, hash*/) {
    var hpoterm = params[0];
    return "http://www.human-phenotype-ontology.org/hpoweb?id=" + hpoterm;
  }

  exports.default = Ember.Helper.helper(genHpoLink);
});
define("varwatch/helpers/gen-pathway-link", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.genPathwayLink = genPathwayLink;
  function genPathwayLink(params /*, hash*/) {
    var path = params[0];
    return "http://www.genome.jp/dbget-bin/www_bget?pathway+" + path.substr(5);
  }

  exports.default = Ember.Helper.helper(genPathwayLink);
});
define("varwatch/helpers/gen-variant-status", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.genVariantStatus = genVariantStatus;
  function genVariantStatus(params /*, hash*/) {
    var status = params[0];
    var matchcount = params[1];
    if (status === "STORED") return "No Matches";else if (status === "MATCHED") {
      if (matchcount === 1) return matchcount + " Match";else return matchcount + " Matches";
    } else return status;
  }

  exports.default = Ember.Helper.helper(genVariantStatus);
});
define('varwatch/helpers/get-hpo-desc', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Helper.extend({
    hpo: Ember.inject.service(),
    compute: function compute(term) {
      if (this.get('hpo').dataloaded) {
        return this.get('hpo').getHpo(term).desc;
      } else {
        return "";
      }
    },

    onHpoLoaded: Ember.observer('hpo.dataloaded', function () {
      this.recompute();
    })
  });
});
define("varwatch/helpers/get-kin-tooltip-text", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.getKinTooltipText = getKinTooltipText;
  function getKinTooltipText(params /*, hash*/) {
    if (params[0] === 1) {
      if (params[1] === "pred") {
        return params[0] + " parent term";
      } else {
        return params[0] + " child term";
      }
    } else if (params[0] === 0) {
      if (params[1] === "pred") {
        return "no parent terms";
      } else {
        return "no child terms";
      }
    } else {
      if (params[1] === "pred") {
        return params[0] + " parent terms";
      } else {
        return params[0] + " child terms";
      }
    }
  }

  exports.default = Ember.Helper.helper(getKinTooltipText);
});
define("varwatch/helpers/getbarclass", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.getbarclass = getbarclass;
  function getbarclass(params /*, hash*/) {
    switch (params[0]) {
      case 4:
        return "bg-fuchsia";
      case 3:
        return "bg-yellow";
      case 2:
        return "bg-blue";
      case 1:
        return "bg-green";
      case 0:
        return "bg-red";
      default:
        return "bg-black";
    }
  }

  exports.default = Ember.Helper.helper(getbarclass);
});
define("varwatch/helpers/getchildbarclass", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.getchildbarclass = getchildbarclass;
  function getchildbarclass(params /*, hash*/) {
    if (params[0] === params[1]) {
      return "bg-cyan";
    } else {
      return "bg-black";
    }
  }

  exports.default = Ember.Helper.helper(getchildbarclass);
});
define('varwatch/helpers/getpredindex', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.getpredindex = getpredindex;
  function getpredindex(params /*, hash*/) {
    for (var i = 0; i < params[1].length; i++) {
      if (params[1][i].term === params[0]) {
        return i;
      }
    }
  }

  exports.default = Ember.Helper.helper(getpredindex);
});
define('varwatch/helpers/hgvsnotgood', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.hgvsnotgood = hgvsnotgood;
  function hgvsnotgood(params /*, hash*/) {
    var rex = new RegExp('^([1-9]|1[0-9]|2[0-2]|[xy]):g.[0-9]{1,10}(_[0-9]{1,10})?(([acgt]{1,20}>[acgt]{1,20}(,[acgt]{1,20})?)|((del[acgt]{0,20})?(ins[acgt]{1,20})?))$', 'i');
    return !rex.test(params[0]);
  }

  exports.default = Ember.Helper.helper(hgvsnotgood);
});
define('varwatch/helpers/hpodescription', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Helper.extend({
    hpo: Ember.inject.service(),
    compute: function compute(term) {
      if (this.get('hpo').dataloaded) {
        return this.get('hpo').getHpo(term[0]).desc;
      } else {
        return "";
      }
    },

    onHpoLoaded: Ember.observer('hpo.dataloaded', function () {
      this.recompute();
    })
  });
});
define('varwatch/helpers/href-to', ['exports', 'ember-href-to/helpers/href-to'], function (exports, _hrefTo) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _hrefTo.default;
    }
  });
  Object.defineProperty(exports, 'hrefTo', {
    enumerable: true,
    get: function () {
      return _hrefTo.hrefTo;
    }
  });
});
define('varwatch/helpers/lf-lock-model', ['exports', 'liquid-fire/helpers/lf-lock-model'], function (exports, _lfLockModel) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _lfLockModel.default;
    }
  });
  Object.defineProperty(exports, 'lfLockModel', {
    enumerable: true,
    get: function () {
      return _lfLockModel.lfLockModel;
    }
  });
});
define('varwatch/helpers/lf-or', ['exports', 'liquid-fire/helpers/lf-or'], function (exports, _lfOr) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _lfOr.default;
    }
  });
  Object.defineProperty(exports, 'lfOr', {
    enumerable: true,
    get: function () {
      return _lfOr.lfOr;
    }
  });
});
define('varwatch/helpers/neq', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var neq = function neq(params) {
    return params[0] !== params[1];
  };

  exports.default = Ember.Helper.helper(neq);
});
define('varwatch/helpers/not', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.not = not;
  function not(params /*, hash*/) {
    return !params[0];
  }

  exports.default = Ember.Helper.helper(not);
});
define('varwatch/helpers/or', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var or = function or(params) {
    return params[0] || params[1];
  };

  exports.default = Ember.Helper.helper(or);
});
define('varwatch/helpers/perform', ['exports', 'ember-concurrency/-helpers'], function (exports, _helpers) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.performHelper = performHelper;
  function performHelper(args, hash) {
    return (0, _helpers.taskHelperClosure)('perform', 'perform', args, hash);
  }

  exports.default = Ember.Helper.helper(performHelper);
});
define('varwatch/helpers/pluralize', ['exports', 'ember-inflector/lib/helpers/pluralize'], function (exports, _pluralize) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _pluralize.default;
});
define('varwatch/helpers/plusone', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var plusone = function plusone(params) {
    return params[0] + 1;
  };

  exports.default = Ember.Helper.helper(plusone);
});
define('varwatch/helpers/singularize', ['exports', 'ember-inflector/lib/helpers/singularize'], function (exports, _singularize) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _singularize.default;
});
define('varwatch/helpers/strip-pathway', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.stripPathway = stripPathway;
  function stripPathway(params /*, hash*/) {
    return params[0].substr(5);
  }

  exports.default = Ember.Helper.helper(stripPathway);
});
define('varwatch/helpers/task', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  function _toConsumableArray(arr) {
    if (Array.isArray(arr)) {
      for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) {
        arr2[i] = arr[i];
      }

      return arr2;
    } else {
      return Array.from(arr);
    }
  }

  function _toArray(arr) {
    return Array.isArray(arr) ? arr : Array.from(arr);
  }

  function taskHelper(_ref) {
    var _ref2 = _toArray(_ref),
        task = _ref2[0],
        args = _ref2.slice(1);

    return task._curry.apply(task, _toConsumableArray(args));
  }

  exports.default = Ember.Helper.helper(taskHelper);
});
define('varwatch/initializers/app-version', ['exports', 'ember-cli-app-version/initializer-factory', 'varwatch/config/environment'], function (exports, _initializerFactory, _environment) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var name = void 0,
      version = void 0;
  if (_environment.default.APP) {
    name = _environment.default.APP.name;
    version = _environment.default.APP.version;
  }

  exports.default = {
    name: 'App Version',
    initialize: (0, _initializerFactory.default)(name, version)
  };
});
define('varwatch/initializers/container-debug-adapter', ['exports', 'ember-resolver/resolvers/classic/container-debug-adapter'], function (exports, _containerDebugAdapter) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: 'container-debug-adapter',

    initialize: function initialize() {
      var app = arguments[1] || arguments[0];

      app.register('container-debug-adapter:main', _containerDebugAdapter.default);
      app.inject('container-debug-adapter:main', 'namespace', 'application:main');
    }
  };
});
define('varwatch/initializers/data-adapter', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: 'data-adapter',
    before: 'store',
    initialize: function initialize() {}
  };
});
define('varwatch/initializers/ember-concurrency', ['exports', 'ember-concurrency'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: 'ember-concurrency',
    initialize: function initialize() {}
  };
});
define('varwatch/initializers/ember-data', ['exports', 'ember-data/setup-container', 'ember-data'], function (exports, _setupContainer) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: 'ember-data',
    initialize: _setupContainer.default
  };
});
define('varwatch/initializers/ember-simple-auth', ['exports', 'varwatch/config/environment', 'ember-simple-auth/configuration', 'ember-simple-auth/initializers/setup-session', 'ember-simple-auth/initializers/setup-session-service'], function (exports, _environment, _configuration, _setupSession, _setupSessionService) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: 'ember-simple-auth',

    initialize: function initialize(registry) {
      var config = _environment.default['ember-simple-auth'] || {};
      config.baseURL = _environment.default.rootURL || _environment.default.baseURL;
      _configuration.default.load(config);

      (0, _setupSession.default)(registry);
      (0, _setupSessionService.default)(registry);
    }
  };
});
define('varwatch/initializers/export-application-global', ['exports', 'varwatch/config/environment'], function (exports, _environment) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.initialize = initialize;
  function initialize() {
    var application = arguments[1] || arguments[0];
    if (_environment.default.exportApplicationGlobal !== false) {
      var theGlobal;
      if (typeof window !== 'undefined') {
        theGlobal = window;
      } else if (typeof global !== 'undefined') {
        theGlobal = global;
      } else if (typeof self !== 'undefined') {
        theGlobal = self;
      } else {
        // no reasonable global, just bail
        return;
      }

      var value = _environment.default.exportApplicationGlobal;
      var globalName;

      if (typeof value === 'string') {
        globalName = value;
      } else {
        globalName = Ember.String.classify(_environment.default.modulePrefix);
      }

      if (!theGlobal[globalName]) {
        theGlobal[globalName] = application;

        application.reopen({
          willDestroy: function willDestroy() {
            this._super.apply(this, arguments);
            delete theGlobal[globalName];
          }
        });
      }
    }
  }

  exports.default = {
    name: 'export-application-global',

    initialize: initialize
  };
});
define('varwatch/initializers/injectStore', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: 'injectStore',
    before: 'store',
    initialize: function initialize() {}
  };
});
define("varwatch/initializers/liquid-fire", ["exports", "liquid-fire/ember-internals", "liquid-fire/velocity-ext"], function (exports, _emberInternals) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  (0, _emberInternals.initialize)();

  exports.default = {
    name: 'liquid-fire',
    initialize: function initialize() {}
  };
});
define('varwatch/initializers/store', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: 'store',
    after: 'ember-data',
    initialize: function initialize() {}
  };
});
define('varwatch/initializers/transforms', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: 'transforms',
    before: 'store',
    initialize: function initialize() {}
  };
});
define("varwatch/instance-initializers/ember-data", ["exports", "ember-data/initialize-store-service"], function (exports, _initializeStoreService) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: "ember-data",
    initialize: _initializeStoreService.default
  };
});
define('varwatch/instance-initializers/ember-href-to', ['exports', 'ember-href-to/href-to'], function (exports, _hrefTo) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  function closestLink(el) {
    if (el.closest) {
      return el.closest('a');
    } else {
      el = el.parentElement;
      while (el && el.tagName !== 'A') {
        el = el.parentElement;
      }
      return el;
    }
  }
  exports.default = {
    name: 'ember-href-to',
    initialize: function initialize(applicationInstance) {
      // we only want this to run in the browser, not in fastboot
      if (typeof FastBoot === "undefined") {
        var hrefToClickHandler = function _hrefToClickHandler(e) {
          var link = e.target.tagName === 'A' ? e.target : closestLink(e.target);
          if (link) {
            var hrefTo = new _hrefTo.default(applicationInstance, e, link);
            hrefTo.maybeHandle();
          }
        };

        document.body.addEventListener('click', hrefToClickHandler);

        // Teardown on app destruction: clean up the event listener to avoid
        // memory leaks.
        applicationInstance.reopen({
          willDestroy: function willDestroy() {
            document.body.removeEventListener('click', hrefToClickHandler);
            return this._super.apply(this, arguments);
          }
        });
      }
    }
  };
});
define('varwatch/instance-initializers/ember-simple-auth', ['exports', 'ember-simple-auth/instance-initializers/setup-session-restoration'], function (exports, _setupSessionRestoration) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = {
    name: 'ember-simple-auth',

    initialize: function initialize(instance) {
      (0, _setupSessionRestoration.default)(instance);
    }
  };
});
define('varwatch/models/annotation', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    identifier: _emberData.default.attr('string'),
    datasets: _emberData.default.hasMany('dataset'),
    matches: _emberData.default.hasMany('match')
  });
});
define('varwatch/models/custom-inflector-rules', ['exports', 'ember-inflector'], function (exports, _emberInflector) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var inflector = _emberInflector.default.inflector;

  inflector.irregular('locus', 'loci');
  inflector.irregular('family', 'families');
  inflector.irregular('variantstatus', 'variantstatuses');
  inflector.uncountable('data');
  inflector.uncountable('positiondata');

  // Meet Ember Inspector's expectation of an export
  exports.default = {};
});
define('varwatch/models/dataset', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    description: _emberData.default.attr('string'),
    ageOfOnset: _emberData.default.attr('string'),
    modeOfInheritance: _emberData.default.attr('string'),
    variants: _emberData.default.hasMany('variant'),
    annotations: _emberData.default.hasMany('annotation'),
    status: _emberData.default.belongsTo('status'),
    statushistory: _emberData.default.belongsTo('statushistory'),
    errorvariants: _emberData.default.hasMany('errorvariant'),
    assembly: _emberData.default.attr('string'),
    statusDatetime: Ember.computed('status.datetime', function () {
      if (this.get('status.datetime')) return this.get('status.datetime').valueOf();else return -1;
    }),
    sortedHpoTerms: Ember.computed.sort('annotations.@each.identifier', function (a, b) {
      if (a.get('identifier') > b.get('identifier')) {
        return 1;
      } else if (a.get('identifier') < b.get('identifier')) {
        return -1;
      }
    })
  });
});
define('varwatch/models/errorvariant', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    identifier: _emberData.default.attr('string'),
    chromosomeName: _emberData.default.attr('string'),
    position: _emberData.default.attr('number'),
    referenceBase: _emberData.default.attr('string'),
    alternateBase: _emberData.default.attr('string'),
    dataset: _emberData.default.belongsTo('dataset'),
    variantstatus: _emberData.default.belongsTo('errorvariantstatus')
  });
});
define('varwatch/models/errorvariantstatus', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    description: _emberData.default.attr('string'),
    status: _emberData.default.attr('string'),
    datetime: _emberData.default.attr('date'),
    errorvariant: _emberData.default.belongsTo('errorvariant'),
    localedatetime: Ember.computed('datetime', function () {
      return this.get('datetime').toLocaleString();
    })
  });
});
define('varwatch/models/family', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    variant: _emberData.default.hasMany('variant'),
    identifier: _emberData.default.attr('string'),
    description: _emberData.default.attr('string'),
    capitalizedDescription: Ember.computed('description', function () {
      var desc = this.get("description");
      if (desc) {
        desc = desc.charAt(0).toUpperCase() + desc.slice(1).toLowerCase();
      }
      return desc;
    })
  });
});
define('varwatch/models/gene', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    variants: _emberData.default.hasMany('variant'),
    identifier: _emberData.default.attr('string'),
    matches: _emberData.default.hasMany('match'),
    ensembl: Ember.computed('identifier', function () {
      var url = "https://rest.ensembl.org/lookup/id/" + this.get('identifier') + "?content-type=application/json";
      var po = _emberData.default.PromiseObject.create({
        promise: Ember.$.getJSON(url)
      });
      return po;
    })
  });
});
define('varwatch/models/match', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    variantstatus: _emberData.default.belongsTo('variantstatus'),
    matchType: _emberData.default.attr('string'),
    matchedVariant: _emberData.default.belongsTo('variant'),
    //identicalMatch: DS.attr('boolean'),
    hpoTerms: _emberData.default.hasMany('annotation'),
    hpoDist: _emberData.default.attr('string'),
    genes: _emberData.default.hasMany('gene'),
    datasetId: _emberData.default.attr('string'),
    queryVariantId: _emberData.default.attr('string'),
    statusId: _emberData.default.attr('string'),
    contact: _emberData.default.attr(),
    database: _emberData.default.attr('string'),
    accIdentifier: _emberData.default.attr('string'),
    acknowledged: _emberData.default.attr('boolean'),
    hpoDistanceTrunc: Ember.computed('hpoDist', function () {
      return new Intl.NumberFormat('en-IN', { minimumFractionDigits: 3, maximumFractionDigits: 3 }).format(this.get('hpoDist'));
    }),
    phenotypeSimilarity: Ember.computed('hpoDist', function () {
      return this.get('hpoDist') > 0.5;
    }),
    monarchScore: Ember.computed('database', 'hpoTerms.[]', 'variantstatus.variant.dataset.annotations.[]', function () {
      if (this.get('database') === 'HGMD' || this.get('database') === 'ClinVar') return { noScore: true };else {
        var firstterms = "";
        this.get('hpoTerms').forEach(function (term, index) {
          if (index > 0) firstterms += "+";
          firstterms += term.get('identifier');
        });
        var secondterms = "";
        if (this.get("variantstatus.variant.dataset.annotations")) {
          this.get("variantstatus.variant.dataset.annotations").forEach(function (term, index) {
            if (index > 0) secondterms += "+";
            secondterms += term.get('identifier');
          });
        }

        if (firstterms && secondterms) {
          var urlltr = "https://monarchinitiative.org/compare/" + firstterms + "/" + secondterms + ".json";
          var urlrtl = "https://monarchinitiative.org/compare/" + secondterms + "/" + firstterms + ".json";
          var po = _emberData.default.PromiseObject.create({
            promise: Promise.all([Ember.$.getJSON(urlltr).then(function (res) {
              return res;
            }, function (reason) {
              return "n/a";
            }), Ember.$.getJSON(urlrtl).then(function (res) {
              return res;
            }, function (reason) {
              return "n/a";
            })]).then(function (values) {
              var res = { ltr: 'n/a', rtl: 'n/a' };
              if (values[0] !== "n/a" && values[0].b && values[0].b[0] !== undefined && values[0].b[0].score !== undefined && values[0].b[0].score.score !== undefined) res.ltr = values[0].b[0].score.score;
              if (values[1] !== "n/a" && values[1].b && values[1].b[0] !== undefined && values[1].b[0].score !== undefined && values[1].b[0].score.score !== undefined) res.rtl = values[1].b[0].score.score;
              var v1 = res.ltr === 'n/a' ? 0 : res.ltr;
              var v2 = res.rtl === 'n/a' ? 0 : res.rtl;
              res.mean = Math.round((v1 + v2) / 2);
              return res;
            })
          });
          return po;
        }
      }
    }),
    statusDatetime: Ember.computed('variantstatus.datetime', function () {
      if (this.get('variantstatus.datetime')) return this.get('variantstatus.datetime').valueOf();else return -1;
    }),
    sortedHpoTerms: Ember.computed.sort('hpoTerms.@each.identifier', function (a, b) {
      if (a.get('identifier') > b.get('identifier')) {
        return 1;
      } else if (a.get('identifier') < b.get('identifier')) {
        return -1;
      }
    })
  });
});
define('varwatch/models/pathway', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    variant: _emberData.default.hasMany('variant'),
    identifier: _emberData.default.attr('string'),
    kegg: Ember.computed('identifier', function () {
      var url = "/kegg/get/" + this.get('identifier');
      var promise = new Ember.RSVP.Promise(function (resolve) {
        Ember.$.get(url).then(function (txt) {
          var lines = txt.split("\n");
          var prefix = "NAME";
          var str = "";
          if (lines[1] && lines[1].startsWith(prefix)) {
            str = lines[1].substr(prefix.length);
            while (str[0] && str[0] === ' ') {
              str = str.substr(1);
            }
            var suffix = " - Homo sapiens (human)";
            if (str.substr(-suffix.length) === suffix) {
              str = str.slice(0, -suffix.length);
            }
            resolve({ description: str });
          } else {
            resolve({ description: "" });
          }
        }, function () {
          resolve({ description: "" });
        });
      });
      var po = _emberData.default.PromiseObject.create({
        promise: promise
      });
      return po;
    })
  });
});
define('varwatch/models/status', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    description: _emberData.default.attr('string'),
    status: _emberData.default.attr('string'),
    datetime: _emberData.default.attr('date'),
    dataset: _emberData.default.belongsTo('dataset'),
    localedatetime: Ember.computed('datetime', function () {
      //console.log(this.get('datetime'));
      //console.log(this.get('datetime').toUTCString());
      return this.get('datetime').toLocaleString();
    })
  });
});
define('varwatch/models/statushistory', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    statushistory: _emberData.default.attr(),
    dataset: _emberData.default.belongsTo('dataset')
  });
});
define('varwatch/models/userinfo', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    firstName: _emberData.default.attr('string'),
    lastName: _emberData.default.attr('string'),
    mail: _emberData.default.attr('string'),
    phone: _emberData.default.attr('string'),
    reportSchedule: _emberData.default.attr('string'),
    institution: _emberData.default.attr('string'),
    address: _emberData.default.attr('string'),
    postalCode: _emberData.default.attr('string'),
    city: _emberData.default.attr('string'),
    country: _emberData.default.attr('string')
  });
});
define('varwatch/models/variant', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    dataset: _emberData.default.belongsTo('dataset'),
    chromosomeName: _emberData.default.attr('string'),
    position: _emberData.default.attr('number'),
    referenceBase: _emberData.default.attr('string'),
    alternateBase: _emberData.default.attr('string'),
    genes: _emberData.default.hasMany('gene'),
    families: _emberData.default.hasMany('family'),
    pathways: _emberData.default.hasMany('pathway'),
    varianthistory: _emberData.default.hasMany('variantstatus'),
    variantstatus: _emberData.default.belongsTo('variantstatus'),
    rawData: _emberData.default.attr('string'),
    ensemblData: Ember.computed('chromosomeName', 'position', 'referenceBase', 'alternateBase', function () {
      var posstart = this.get('position');
      var posend = posstart + this.get('referenceBase').length - 1;
      var region = this.get('chromosomeName') + ":" + posstart + ":" + posend;
      var allele = this.get('alternateBase').length > 0 ? this.get('alternateBase') : "DEL";
      var url = "https://rest.ensembl.org/vep/human/region/" + region + "/" + allele + "?content-type=application/json&canonical=1&xref_refseq=1";
      //console.log(url);
      var po = _emberData.default.PromiseObject.create({
        promise: Ember.RSVP.resolve(Ember.$.getJSON(url))
      });
      return po;
    })
  });
});
define('varwatch/models/variantstatus', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.Model.extend({
    description: _emberData.default.attr('string'),
    status: _emberData.default.attr('string'),
    datetime: _emberData.default.attr('date'),
    variant: _emberData.default.belongsTo('variant', { polymorphic: true, inverse: null }),
    match: _emberData.default.belongsTo('match'),
    matchType: Ember.computed('match.matchType', function () {
      return this.get('match.matchType');
    }),
    matchHpoDist: Ember.computed('match.hpoDist', function () {
      return this.get('match.hpoDist');
    }),
    matchDatabase: Ember.computed('match.database', function () {
      return this.get('match.database');
    }),
    matchId: Ember.computed('match.id', function () {
      return this.get('match.id');
    }),
    localedatetime: Ember.computed('datetime', function () {
      return this.get('datetime').toLocaleString();
    })
  });
});
define('varwatch/reopens/text-field', [], function () {
  'use strict';

  Ember.TextField.reopen({
    attributeBindings: ['aria-describedby']
  });
});
define('varwatch/resolver', ['exports', 'ember-resolver'], function (exports, _emberResolver) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberResolver.default;
});
define('varwatch/router', ['exports', 'varwatch/config/environment'], function (exports, _environment) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var Router = Ember.Router.extend({
    location: _environment.default.locationType,
    rootURL: _environment.default.rootURL
  });

  Router.map(function () {
    this.route('index', { path: '/' });
    this.route('login');
    this.route('datasets', function () {
      this.route('details', { path: '/:dataset_id' });
    });
    this.route('registration');
    this.route('userinfo');
    this.route('submit');
    this.route('recover');
    this.route('help');
    this.route('not-found', { path: '/*path' });
    this.route('oauth');
    this.route('redirect');
    this.route('convert');
    this.route('impressum');
    //to enable testdata uncomment route, enable link in header and move testdata files to pupic assets
    //this.route('testdata');
  });

  exports.default = Router;
});
define('varwatch/routes/application', ['exports', 'ember-simple-auth/mixins/application-route-mixin'], function (exports, _applicationRouteMixin) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend(_applicationRouteMixin.default, {
    sessionAuthenticated: function sessionAuthenticated() {
      return this._super.apply(this, arguments);
    }
  });
});
define('varwatch/routes/convert', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend({
    setupController: function setupController(controller, model) {
      this.controllerFor('application').set('navigation', [{ name: "Home", link: "index" }, { name: "HGVS Converter", link: "" }]);
      this._super.apply(this, arguments);
    }
  });
});
define('varwatch/routes/datasets', ['exports', 'ember-simple-auth/mixins/authenticated-route-mixin'], function (exports, _authenticatedRouteMixin) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend(_authenticatedRouteMixin.default, {});
});
define('varwatch/routes/datasets/details', ['exports', 'ember-simple-auth/mixins/authenticated-route-mixin'], function (exports, _authenticatedRouteMixin) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend(_authenticatedRouteMixin.default, {
    queryParams: {
      details: {
        replace: true
      }
    },
    actions: {
      error: function error(_error, transition) {
        this.transitionTo('datasets');
        return;
      }
    },
    model: function model(params) {
      //console.log('executing model hook');
      return this.store.findRecord('dataset', params.dataset_id, { backgreload: true }).then(function (dataset) {
        //console.log('plz reload');
        dataset.get('variants').reload();
        return dataset;
      });
    },
    setupController: function setupController() {
      this.controllerFor('application').set('navigation', [{ name: "Home", link: "index" }, { name: "Datasets", link: "datasets" }, { name: "Details", link: "" }]);
      return this._super.apply(this, arguments);
    }
  });
});
define('varwatch/routes/datasets/index', ['exports', 'ember-simple-auth/mixins/authenticated-route-mixin'], function (exports, _authenticatedRouteMixin) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend(_authenticatedRouteMixin.default, {
    actions: {
      willTransition: function willTransition(transition) {
        this.get('controller').get('taskRefreshModel').cancelAll();
        return true;
      }
    },
    model: function model() {
      return this.store.findAll('dataset', { backgroundReload: true });
    },
    setupController: function setupController(controller, model) {
      this.controllerFor('application').set('navigation', [{ name: "Home", link: "index" }, { name: "Datasets", link: "" }]);
      this._super.apply(this, arguments);
      controller.get('taskRefreshModel').perform();
    }
  });
});
define('varwatch/routes/help', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend({
    session: Ember.inject.service(),
    setupController: function setupController() {
      if (this.get('session.isAuthenticated')) this.controllerFor('application').set('navigation', [{ name: "Home", link: "index" }, { name: "Help", link: "" }]);
      return this._super.apply(this, arguments);
    }
  });
});
define('varwatch/routes/impressum', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend({});
});
define('varwatch/routes/index', ['exports', 'ember-simple-auth/mixins/authenticated-route-mixin'], function (exports, _authenticatedRouteMixin) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend(_authenticatedRouteMixin.default, {
    /*model() {
      //this.store.adapterFor('match').set('namespace', 'varwatch/api/matching/new');
    },*/
    setupController: function setupController() {
      this.controllerFor('application').set('navigation', [{ name: "Home", link: "" }]);
      //this.controllerFor('index').loadModel();
      return this._super.apply(this, arguments);
    },
    afterModel: function afterModel() {
      this.controllerFor('index').loadModel();
      return this._super.apply(this, arguments);
    },

    actions: {
      /*loading(transition) {  // second parameter "originRoute" possible
        let controller = this.controllerFor('index');
        controller.set('currentlyLoading', true);
        transition.promise.finally(function() {
            controller.set('currentlyLoading', false);
        });
      }*/
    }
  });
});
define('varwatch/routes/login', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend({
    setupController: function setupController() {
      this.controllerFor('application').set('navigation', []);
      return this._super.apply(this, arguments);
    }
  });
});
define('varwatch/routes/not-found', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend({});
});
define('varwatch/routes/oauth', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend({
    setupController: function setupController() {
      this._super.apply(this, arguments);
      this.controllerFor('oauth').checkParams();
    }
  });
});
define('varwatch/routes/recover', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend({
    setupController: function setupController() {
      this.controllerFor('application').set('navigation', []);
      return this._super.apply(this, arguments);
    }
  });
});
define('varwatch/routes/redirect', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend({
    model: function model() {
      var res = {};
      var hash = window.location.hash.substr(1);
      var params = hash.split('&');
      params.forEach(function (param) {
        var p = param.split('=');
        if (p.length === 2) {
          res[p[0]] = p[1];
        }
      });
      //console.log(res);
      return res;
    }
  });
});
define('varwatch/routes/registration', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend({
    setupController: function setupController() {
      this.controllerFor('application').set('navigation', []);
      return this._super.apply(this, arguments);
    }
  });
});
define('varwatch/routes/submit', ['exports', 'ember-simple-auth/mixins/authenticated-route-mixin'], function (exports, _authenticatedRouteMixin) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend(_authenticatedRouteMixin.default, {
    setupController: function setupController() {
      this.controllerFor('application').set('navigation', [{ name: "Home", link: "index" }, { name: "Submit", link: "" }]);
      return this._super.apply(this, arguments);
    },

    actions: {
      didTransition: function didTransition() {
        Ember.run.schedule('afterRender', this, this.setTooltips);
        return true;
      }
    },
    setTooltips: function setTooltips() {
      Ember.$('[data-toggle="tooltip"]').tooltip({ container: "body" });
    }
  });
});
define('varwatch/routes/testdata', ['exports', 'ember-simple-auth/mixins/authenticated-route-mixin'], function (exports, _authenticatedRouteMixin) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend(_authenticatedRouteMixin.default, {
    setupController: function setupController() {
      this.controllerFor('application').set('navigation', [{ name: "Home", link: "index" }, { name: "Test Data", link: "" }]);
      return this._super.apply(this, arguments);
    }
  });
});
define('varwatch/routes/userinfo', ['exports', 'ember-simple-auth/mixins/authenticated-route-mixin'], function (exports, _authenticatedRouteMixin) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Route.extend(_authenticatedRouteMixin.default, {
    model: function model() {
      return this.store.findAll('userinfo', { reload: true });
    },
    setupController: function setupController(controller, model) {
      /*let shed = model.objectAt(0).get('reportSchedule');
          console.log(model.objectAt(0).changedAttributes());
       if(model.objectAt(0).get("hasDirtyAttributes")) {
        model.objectAt(0).rollbackAttributes();
      }
      console.log(model.objectAt(0).changedAttributes());*/
      controller.set('shed', model.objectAt(0).get('reportSchedule'));
      controller.set('model', model.objectAt(0));
      //console.log(controller.get('shed'));
      this.controllerFor('application').set('navigation', [{ name: "Home", link: "index" }, { name: "User Information", link: "" }]);
      //return this._super(...arguments);
    }
  });
});
define('varwatch/serializers/annotation', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    primaryKey: 'identifier',
    normalizeResponse: function normalizeResponse(store, primaryModelClass, payload, id, requestType) {
      /*console.log("normresponse annotation!!!");
      console.log(store);
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);*/
      var newpayload = {
        annotation: payload
      };
      //console.log("normresponse annotation!!!")
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/serializers/dataset', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    normalizeSingleResponse: function normalizeSingleResponse(store, primaryModelClass, payload, id, requestType) {
      /*console.log("dataset normresponse!!!");
      console.log(store);
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);*/
      payload.links = {
        variants: '/varwatch/api/information/datasets/' + payload.id + '/variants',
        annotations: '/varwatch/api/annotation/datasets/' + payload.id,
        status: '/varwatch/api/status/datasets/' + payload.id,
        statushistory: '/varwatch/api/status/datasets/' + payload.id + '/all',
        errorvariants: '/varwatch/api/information/datasets/' + payload.id + '/errorvariants'
      };
      var newpayload = {
        dataset: payload
      };
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    },
    normalizeArrayResponse: function normalizeArrayResponse(store, primaryModelClass, payload, id, requestType) {
      /*console.log("dataset normresponse!!!");
      console.log(store);
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);*/
      var newpayload = {
        datasets: payload
      };
      newpayload.datasets.forEach(function (dataset) {
        dataset.links = {
          variants: '/varwatch/api/information/datasets/' + dataset.id + '/variants',
          annotations: '/varwatch/api/annotation/datasets/' + dataset.id,
          status: '/varwatch/api/status/datasets/' + dataset.id,
          statushistory: '/varwatch/api/status/datasets/' + dataset.id + '/all',
          errorvariants: '/varwatch/api/information/datasets/' + dataset.id + '/errorvariants'
        };
      });
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/serializers/errorvariant', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend(_emberData.default.EmbeddedRecordsMixin, {
    attrs: {
      variantstatus: { embedded: 'always' }
    },
    normalizeResponse: function normalizeResponse(store, primaryModelClass, payload, id, requestType) {
      var _this = this;

      /*console.log("normresponse errorvariant!!!");
      console.log(store);
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);*/
      var newpayload = {
        errorvariants: []
      };
      //console.log(payload);
      //console.log(payload[0].status.id);
      payload.forEach(function (ev) {
        var slicesid = _this.sliceIdentifier(ev.identifier);
        var nev = {
          id: ev.status.id,
          identifier: ev.identifier,
          chromosomeName: slicesid.chromosomeName,
          position: slicesid.position,
          referenceBase: slicesid.referenceBase,
          alternateBase: slicesid.alternateBase,
          variantstatus: ev.status
        };
        newpayload.errorvariants.push(nev);
      });
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    },
    sliceIdentifier: function sliceIdentifier(identifier) {
      var res = {
        chromosomeName: "",
        position: "",
        referenceBase: "",
        alternateBase: ""
      };
      if (identifier !== "NA") {
        var items = identifier.split('_');
        if (items.length > 2) {
          var refalt = items[2].split('/');
          if (refalt.length === 2) {
            res.chromosomeName = items[0];
            res.position = items[1];
            res.referenceBase = refalt[0];
            res.alternateBase = refalt[1];
          }
        }
      }
      return res;
    }
  });
});
define('varwatch/serializers/errorvariantstatus', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    normalizeSingleResponse: function normalizeSingleResponse(store, primaryModelClass, payload, id, requestType) {
      //console.log("SINGLE normresponse errorvariantstatus!!!");
      var newpayload = {
        variantstatus: payload
      };
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    },
    normalizeArrayResponse: function normalizeArrayResponse(store, primaryModelClass, payload, id, requestType) {
      //console.log("ARRAY normresponse errorvariantstatus!!!");
      var newpayload = {
        variantstatuses: payload
      };
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/serializers/family', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    primaryKey: 'identifier',
    normalizeResponse: function normalizeResponse(store, primaryModelClass, payload, id, requestType) {
      var newpayload = {
        family: payload
      };
      //console.log("normresponse fam!!!")
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/serializers/gene', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    primaryKey: 'identifier',
    normalizeResponse: function normalizeResponse(store, primaryModelClass, payload, id, requestType) {
      var newpayload = {
        gene: payload
      };
      //console.log("normresponse gen!!!")
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/serializers/match', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend(_emberData.default.EmbeddedRecordsMixin, {
    attrs: {
      genes: { embedded: 'always' },
      hpoTerms: { embedded: 'always' },
      matchedVariant: { embedded: 'always' }
    },
    normalizeSingleResponse: function normalizeSingleResponse(store, primaryModelClass, payload, id, requestType) {
      //console.log("normsingleresponse match!!!");
      /*console.log(store);
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);
      console.log(payload);*/
      //payload.links = {
      //variantstatus: `/varwatch/api/status/${payload.statusId}`
      //};
      payload.variantstatus = payload.statusId;
      //if(!payload.database) payload.database='VarWatch';
      var newpayload = {
        match: payload
      };
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    },
    normalizeArrayResponse: function normalizeArrayResponse(store, primaryModelClass, payload, id, requestType) {

      //console.log("normarrayresponse match!!!");
      /*console.log(store);
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);
      console.log(payload);*/
      payload.forEach(function (match) {
        //match.links = {
        //variantstatus: `/varwatch/api/status/${match.statusId}`
        //};
        match.variantstatus = match.statusId;
        //if(!match.database) match.database='VarWatch';
      });
      var newpayload = {
        match: payload
      };
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/serializers/pathway', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    primaryKey: 'identifier',
    normalizeResponse: function normalizeResponse(store, primaryModelClass, payload, id, requestType) {
      var newpayload = {
        pathway: payload
      };
      //console.log("normresponse path!!!")
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/serializers/status', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    normalizeSingleResponse: function normalizeSingleResponse(store, primaryModelClass, payload, id, requestType) {
      /*console.log("normresponse status!!!");
      console.log(store);
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);*/
      var newpayload = {
        status: payload
      };
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/serializers/statushistory', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    normalizeFindBelongsToResponse: function normalizeFindBelongsToResponse(store, primaryModelClass, payload, id, requestType) {
      /*console.log("normresponse status!!!");
      console.log(store);
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);*/
      //console.log(payload);
      //console.log(payload.payload);
      payload.payload.forEach(function (status) {
        var ms = Date.parse(status.datetime);
        status.datetime = new Date(ms);
        status.localedatetime = status.datetime.toLocaleString();
      });
      var newpayload = {
        statushistory: {
          id: payload.id,
          statushistory: payload.payload
        }
      };
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/serializers/userinfo', ['exports', 'ember-data'], function (exports, _emberData) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    primaryKey: 'mail',
    normalizeResponse: function normalizeResponse(store, primaryModelClass, payload, id, requestType) {
      /*console.log("userinforesponse match!!!");
      console.log(store);
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);*/
      //console.log(payload);
      var newpayload = {
        userinfo: payload
      };
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    },
    serializeIntoHash: function serializeIntoHash(hash, typeClass, snapshot, options) {
      var payload = this.serialize(snapshot, options);
      for (var key in payload) {
        if (key !== 'reportSchedule' && key !== 'mail') {
          hash[key] = payload[key];
        }
      }
    }
  });
});
define("varwatch/serializers/variant", ["exports", "ember-data"], function (exports, _emberData) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    normalizeArrayResponse: function normalizeArrayResponse(store, primaryModelClass, payload, id, requestType) {
      //console.log("normresponse variants!!!");
      /*
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);
      */
      //console.log(payload);

      var newpayload = {
        variants: payload
      };
      newpayload.variants.forEach(function (variant) {
        variant.metaData.forEach(function (md) {
          if (md.dataKey == "raw_variant") {
            variant.rawData = md.dataValue;
          }
        });
        variant.links = {
          variantstatus: "/varwatch/api/status/variants/" + variant.id,
          genes: "/varwatch/api/annotation/variants/" + variant.id + "/genes",
          pathways: "/varwatch/api/annotation/variants/" + variant.id + "/pathways",
          families: "/varwatch/api/annotation/variants/" + variant.id + "/families",
          varianthistory: "/varwatch/api/status/variants/" + variant.id + "/all"
          //dataset: `/varwatch/api/information/datasets/${variant.datasetId}`
        };
        variant.dataset = variant.datasetId;
      });
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    },
    normalizeSingleResponse: function normalizeSingleResponse(store, primaryModelClass, payload, id, requestType) {
      //console.log("normresponse single variants");
      /*
      console.log(primaryModelClass);
      console.log(payload);
      console.log(id);
      console.log(requestType);
      */
      //console.log(payload);

      payload.metaData.forEach(function (md) {
        if (md.dataKey == "raw_variant") {
          payload.rawData = md.dataValue;
        }
      });
      payload.links = {
        variantstatus: "/varwatch/api/status/variants/" + payload.id,
        genes: "/varwatch/api/annotation/variants/" + payload.id + "/genes",
        pathways: "/varwatch/api/annotation/variants/" + payload.id + "/pathways",
        families: "/varwatch/api/annotation/variants/" + payload.id + "/families",
        varianthistory: "/varwatch/api/status/variants/" + payload.id + "/all"
        //dataset: `/varwatch/api/information/datasets/${payload.datasetId}`
      };
      payload.dataset = payload.datasetId;
      var newpayload = {
        variant: payload
      };
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    },
    normalize: function normalize(typeClass, hash, prop) {
      if (!prop && !hash.dataset && !(hash.datasetId == null)) {
        hash.dataset = hash.datasetId;
        hash.links = {
          variantstatus: "/varwatch/api/status/variants/" + hash.id,
          genes: "/varwatch/api/annotation/variants/" + hash.id + "/genes",
          pathways: "/varwatch/api/annotation/variants/" + hash.id + "/pathways",
          families: "/varwatch/api/annotation/variants/" + hash.id + "/families",
          varianthistory: "/varwatch/api/status/variants/" + hash.id + "/all"
        };
      }
      return this._super(typeClass, hash, prop);
    }
  });
});
define("varwatch/serializers/variantstatus", ["exports", "ember-data"], function (exports, _emberData) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _emberData.default.RESTSerializer.extend({
    normalizeSingleResponse: function normalizeSingleResponse(store, primaryModelClass, payload, id, requestType) {
      //console.log("SINGLE normresponse variantstatus!!!");
      //console.log(this.makeLink(payload.id,payload.payload.id));
      //if (payload.matchVariantId===undefined) console.log(payload);
      var newpayload = {
        variantstatus: payload
      };
      if (newpayload.variantstatus.status === "MATCHED") {
        newpayload.variantstatus.links = {
          //match: `/varwatch/api/matching/matches/${payload.matchVariantId}`,
          //variant: `/varwatch/api/information/variants/${payload.variantId}`
        };
        newpayload.variantstatus.variant = payload.variantId;
        newpayload.variantstatus.match = payload.matchVariantId;
      }
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    },
    normalizeArrayResponse: function normalizeArrayResponse(store, primaryModelClass, payload, id, requestType) {
      //console.log("ARRAY normresponse variantstatus!!!");
      payload.forEach(function (status) {
        if (status.status === "MATCHED") {
          //if (status.matchVariantId===undefined) console.log(status);
          //console.log(this.makeLink(payload.id,status.id));
          status.links = {
            //match:  `/varwatch/api/matching/matches/${status.matchVariantId}`,
            //variant: `/varwatch/api/information/variants/${status.variantId}`
          };
          status.variant = status.variantId;
          status.match = status.matchVariantId;
        }
      });
      var newpayload = {
        variantstatuses: payload
      };
      //console.log(newpayload);
      return this._super(store, primaryModelClass, newpayload, id, requestType);
    }
  });
});
define('varwatch/services/ajax', ['exports', 'ember-ajax/services/ajax'], function (exports, _ajax) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _ajax.default;
    }
  });
});
define('varwatch/services/cookies', ['exports', 'ember-cookies/services/cookies'], function (exports, _cookies) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _cookies.default;
});
define('varwatch/services/ember-elsewhere', ['exports', 'ember-elsewhere/services/ember-elsewhere'], function (exports, _emberElsewhere) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _emberElsewhere.default;
    }
  });
});
define('varwatch/services/hpo', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.Service.extend({
    dataloaded: false,
    init: function init() {
      var _this = this;

      var hpodata = {};
      Ember.$.get("/varwatch/api/hpo/data", function (data) {
        //console.log(data);
        var lines = data.split("\n");
        //console.log(lines);
        //let maxpred=0;
        while (lines.length > 0 && lines[0].startsWith('#')) {
          var line1 = lines.shift();
          if (line1.toLowerCase().indexOf('version') !== -1) {
            var line1tokens = line1.split(':');
            if (line1tokens.length === 2) {
              _this.set('hpoVersion', line1tokens[1]).trim();
            }
          }
        }
        lines.forEach(function (line) {
          var tokens = line.split('|');
          if (tokens[0].length > 3) {
            var terms = tokens[0].split(',');
            var hpo = terms.shift();
            var desc = tokens[1];
            var pred = [];
            if (tokens[2]) {
              pred = tokens[2].split(',');
            }
            var obj = {
              term: hpo,
              desc: desc,
              pred: pred,
              succ: []
            };
            hpodata[hpo] = obj;
            terms.forEach(function (altterm) {
              return hpodata[altterm] = { term: altterm, isAltOf: hpo };
            });
          }
          //maxpred=Math.max(maxpred, pred.length);
          //if(pred.length===5) {console.log(hpo)};
        });
        Object.keys(hpodata).forEach(function (key) {
          var hpo = hpodata[key];
          if (!hpo.isAltOf) {
            hpo.pred.forEach(function (pred) {
              hpodata[pred].succ.push(hpo.term);
            });
          }
        });
        _this.set('hpoData', hpodata);
        _this.set('dataloaded', true);
      }, 'text');
      this._super.apply(this, arguments);
    },
    searchHpos: function searchHpos(term) {
      var _this2 = this;

      var lterm = term.toLowerCase();
      var res = [];
      if (term.length > 2) {
        Object.keys(this.get('hpoData')).forEach(function (key) {
          var hpo = _this2.get('hpoData')[key];
          if (hpo.desc && hpo.desc.toLowerCase().indexOf(lterm) >= 0 || hpo.term.toLowerCase().indexOf(lterm) >= 0) {
            res.push(hpo);
          }
        });
      }
      return res.sort(function (a, b) {
        return b.term < a.term;
      });
    },
    getHpo: function getHpo(hpoterm) {
      return this.get('hpoData')[hpoterm];
    },
    getChildHpos: function getChildHpos(hpoterm) {
      var _this3 = this;

      var childterms = this.get('hpoData')[hpoterm].succ;
      var children = [];
      childterms.forEach(function (cterm) {
        children.push(_this3.get('hpoData')[cterm]);
      });
      return children;
    },
    getParentHpos: function getParentHpos(hpoterm) {
      var _this4 = this;

      var parentterms = this.get('hpoData')[hpoterm].pred;
      var parents = [];
      parentterms.forEach(function (pterm) {
        parents.push(_this4.get('hpoData')[pterm]);
      });
      return parents;
    },
    getSiblingHpos: function getSiblingHpos(hpoterm) {
      var _this5 = this;

      var parents = this.getParentHpos(hpoterm);
      var siblings = [];
      parents.forEach(function (phpo) {
        Array.prototype.push.apply(siblings, _this5.getChildHpos(phpo.term).filter(function (hpo) {
          return hpo.term !== hpoterm && siblings.every(function (x) {
            return x.term !== hpo.term;
          });
        }));
      });
      siblings.sort(function (a, b) {
        if (a.term < b.term) {
          return -1;
        } else if (a.term > b.term) {
          return 1;
        } else {
          return 0;
        }
      });
      return siblings;
    },
    isPhenotype: function isPhenotype(hpoterm) {
      var hpo = this.getHpo(hpoterm);
      if (!hpo) {
        return false;
      }
      if (hpo.isAltOf) hpo = this.getHpo(hpo.isAltOf);
      while (hpo.term !== "HP:0000001") {
        if (hpo.term === "HP:0000118") {
          return true;
        } else {
          hpo = this.getHpo(hpo.pred[0]);
        }
      }
      return false;
    },
    parseStringForPhenotypes: function parseStringForPhenotypes(str) {
      var _this6 = this;

      var res = { ok: [], failed: [], replaced: [] };
      str = str.trim();
      if (str.length > 0) {
        var regexp = new RegExp('hp:[0-9]{7}', 'img');
        var terms = str.match(regexp);
        if (terms) {
          terms.forEach(function (term) {
            if (_this6.isPhenotype(term)) {
              if (_this6.getHpo(term).isAltOf) {
                if (res.replaced.indexOf(term) === -1) {
                  res.replaced.push(term);
                }
                if (res.ok.indexOf(_this6.getHpo(term).isAltOf) === -1) {
                  res.ok.push(_this6.getHpo(term).isAltOf);
                }
              } else {
                if (res.ok.indexOf(term) === -1) {
                  res.ok.push(term);
                }
              }
            } else if (res.failed.indexOf(term) === -1) {
              res.failed.push(term.toUpperCase());
            }
          });
        }
      }
      res.ok.sort();
      res.failed.sort();
      res.replaced.sort();
      return res;
    },

    onsetData: Ember.computed('dataloaded', function () {
      var _this7 = this;

      if (this.get('dataloaded')) {
        var terms = this.getAllChildHpoTerms('HP:0003674');
        var res = [];
        terms.forEach(function (term) {
          var hpo = _this7.getHpo(term);
          res.push({
            term: hpo.term,
            desc: hpo.desc
          });
        });
        return res;
      } else {
        return [];
      }
    }),
    inheritanceData: Ember.computed('dataloaded', function () {
      var _this8 = this;

      if (this.get('dataloaded')) {
        var terms = this.getAllChildHpoTerms('HP:0000005');
        var res = [];
        terms.forEach(function (term) {
          var hpo = _this8.getHpo(term);
          res.push({
            term: hpo.term,
            desc: hpo.desc
          });
        });
        return res;
      } else {
        return [];
      }
    }),
    getAllChildHpoTerms: function getAllChildHpoTerms(hpoterm) {
      var _this9 = this;

      var childhpos = this.getChildHpos(hpoterm);
      var res = [];
      childhpos.forEach(function (hpo) {
        var term = hpo.term;
        if (res.indexOf(term) === -1) {
          res.push(term);
          var grandchildren = _this9.getAllChildHpoTerms(term);
          grandchildren.forEach(function (gchild) {
            if (res.indexOf(gchild) === -1) res.push(gchild);
          });
        }
      });
      return res.sort();
    }
  });
});
define("varwatch/services/liquid-fire-transitions", ["exports", "liquid-fire/transition-map"], function (exports, _transitionMap) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _transitionMap.default;
});
define('varwatch/services/session', ['exports', 'ember-simple-auth/services/session'], function (exports, _session) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _session.default;
});
define('varwatch/session-stores/application', ['exports', 'ember-simple-auth/session-stores/adaptive'], function (exports, _adaptive) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = _adaptive.default.extend();
});
define("varwatch/templates/application", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "3X5IHy+Y", "block": "{\"symbols\":[\"nav\"],\"statements\":[[6,\"div\"],[9,\"id\",\"top\"],[9,\"class\",\"all-container\"],[7],[0,\"\\n  \"],[6,\"nav\"],[9,\"class\",\"navbar navbar-default flex0\"],[9,\"role\",\"navigation\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"fluid-container\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"navbar-header\"],[7],[0,\"\\n        \"],[6,\"a\"],[9,\"class\",\"navbar-brand\"],[10,\"href\",[26,[[25,\"href-to\",[\"index\"],null]]]],[7],[0,\"\\n          \"],[6,\"img\"],[9,\"class\",\"navbar-logo\"],[9,\"alt\",\"Varwatch\"],[9,\"src\",\"/assets/images/logo.png\"],[7],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"navbar\"],[7],[0,\"\\n        \"],[6,\"ul\"],[9,\"class\",\"nav navbar-nav\"],[7],[0,\"\\n\"],[4,\"if\",[[20,[\"navigation\"]]],null,{\"statements\":[[0,\"            \"],[6,\"li\"],[7],[6,\"ul\"],[9,\"class\",\"breadcrumb list-inline transparent\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"navigation\"]]],null,{\"statements\":[[4,\"if\",[[19,1,[\"link\"]]],null,{\"statements\":[[0,\"                  \"],[6,\"li\"],[7],[4,\"link-to\",[[19,1,[\"link\"]]],null,{\"statements\":[[1,[19,1,[\"name\"]],false]],\"parameters\":[]},null],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                  \"],[6,\"li\"],[9,\"class\",\"active\"],[7],[1,[19,1,[\"name\"]],false],[8],[0,\"\\n\"]],\"parameters\":[]}]],\"parameters\":[1]},null],[0,\"            \"],[8],[0,\" \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"        \"],[8],[0,\"\\n\"],[4,\"if\",[[25,\"neq\",[[20,[\"target\",\"currentPath\"]],\"oauth\"],null]],null,{\"statements\":[[0,\"          \"],[6,\"ul\"],[9,\"class\",\"nav navbar-nav navbar-right\"],[7],[0,\" \\n\"],[4,\"if\",[[20,[\"session\",\"isAuthenticated\"]]],null,{\"statements\":[[0,\"              \"],[2,\"\\n              <li>\\n                {{#link-to 'testdata'}}Test Data{{/link-to}}\\n              </li>\\n              \"],[0,\"\\n              \"],[6,\"li\"],[7],[0,\"\\n                \"],[4,\"link-to\",[\"convert\"],null,{\"statements\":[[0,\"HGVS Converter\"]],\"parameters\":[]},null],[0,\"\\n              \"],[8],[0,\"\\n              \"],[6,\"li\"],[7],[0,\"\\n                \"],[4,\"link-to\",[\"help\"],null,{\"statements\":[[0,\"Help\"]],\"parameters\":[]},null],[0,\"\\n              \"],[8],[0,\"\\n              \"],[6,\"li\"],[7],[0,\"\\n                \"],[6,\"button\"],[9,\"class\",\"btn btn-default navbar-btn\"],[3,\"action\",[[19,0,[]],\"invalidateSession\"]],[7],[0,\"Logout\"],[8],[0,\"\\n              \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"              \"],[6,\"li\"],[7],[0,\"\\n                \"],[4,\"link-to\",[\"registration\"],null,{\"statements\":[[0,\"Register\"]],\"parameters\":[]},null],[0,\"\\n              \"],[8],[0,\"\\n              \"],[6,\"li\"],[7],[0,\"\\n                \"],[4,\"link-to\",[\"help\"],null,{\"statements\":[[0,\"Help\"]],\"parameters\":[]},null],[0,\"\\n              \"],[8],[0,\"\\n              \"],[6,\"li\"],[7],[0,\"\\n                \"],[4,\"link-to\",[\"login\"],null,{\"statements\":[[0,\"Sign in\"]],\"parameters\":[]},null],[0,\"\\n              \"],[8],[0,\"\\n\"]],\"parameters\":[]}],[0,\"          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[2,\"\\n    <div class=\\\"demoserver-overlay\\\">\\n      <h1>\\n        DEMOSERVER\\n      </h1>\\n    </div>\\n    \"],[0,\"\\n  \"],[8],[0,\"\\n  \"],[1,[25,\"liquid-outlet\",null,[[\"class\"],[\"flex1\"]]],false],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"varwatch-footer flex0\"],[7],[0,\"\\n    \"],[4,\"link-to\",[\"impressum\"],null,{\"statements\":[[0,\"Impressum\"]],\"parameters\":[]},null],[0,\"\\n    \"],[6,\"a\"],[9,\"href\",\"mailto:info@varwatch.de\"],[7],[0,\"Support\"],[8],[0,\"\\n    \"],[2,\"\\n      <div class = \\\"logo\\\">\\n        <img class = \\\"uksh-logo\\\" src=\\\"/assets/images/ukshlogo.png\\\" alt=\\\"UKSH Logo\\\">\\n      </div>\\n      <div class = \\\"logo\\\">\\n        <img class = \\\"ikmb-logo\\\" src=\\\"/assets/images/cau-norm-rgb-0720.png\\\" alt=\\\"CAU Logo\\\">\\n      </div>\\n      <div class = \\\"logo\\\">\\n        <img class = \\\"bmbf-logo\\\" src=\\\"/assets/images/bmbflogo.png\\\" alt=\\\"BMBF Logo\\\">\\n      </div>\\n    \"],[0,\"\\n  \"],[8],[0,\"\\n  \"],[1,[18,\"modal-target\"],false],[0,\"\\n\"],[8],[0,\"\\n\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/application.hbs" } });
});
define("varwatch/templates/components/bootstrap-fileselect", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "cnqMr4Qs", "block": "{\"symbols\":[],\"statements\":[[0,\"\\n    \"],[6,\"label\"],[9,\"class\",\"btn btn-default btn-file\"],[7],[0,\"\\n      Browse...\"],[6,\"input\"],[9,\"type\",\"file\"],[9,\"class\",\"hidden\"],[7],[8],[0,\"\\n    \"],[8],[0,\" \"],[1,[25,\"if\",[[20,[\"file\"]],[20,[\"file\",\"name\"]],\"No file selected.\"],null],false],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/bootstrap-fileselect.hbs" } });
});
define("varwatch/templates/components/elide-cell", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "A6rYFIbM", "block": "{\"symbols\":[],\"statements\":[[4,\"if\",[[20,[\"textElided\"]]],null,{\"statements\":[[0,\"  \"],[6,\"span\"],[9,\"class\",\"no-bottom-margin\"],[9,\"data-toggle\",\"tooltip\"],[10,\"title\",[18,\"text\"],null],[7],[1,[18,\"displayText\"],false],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"  \"],[1,[18,\"displayText\"],false],[0,\"\\n\"]],\"parameters\":[]}]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/elide-cell.hbs" } });
});
define("varwatch/templates/components/ensembl-data", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "C1yjttCC", "block": "{\"symbols\":[],\"statements\":[[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[20,[\"ensembl\",\"description\"]],null],[7],[1,[20,[\"ensembl\",\"display_name\"]],false],[8]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/ensembl-data.hbs" } });
});
define("varwatch/templates/components/form-group", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "NFc6Bp6g", "block": "{\"symbols\":[\"&default\"],\"statements\":[[11,1],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/form-group.hbs" } });
});
define("varwatch/templates/components/help-page", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "sTI+Mij6", "block": "{\"symbols\":[],\"statements\":[[6,\"div\"],[9,\"class\",\"container\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"row\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"col-md-12\"],[7],[0,\"\\n    \"],[6,\"h1\"],[7],[0,\"Bedienungsanleitung\"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"row\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"col-md-9\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section0\"],[7],[0,\"    \\n        \"],[6,\"h2\"],[7],[0,\"Unterstützte Browser\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"Die VarWatch-Webseite ist für die Nutzung der gängigen Desktop-Browser (Chrome, Firefox, Safari, Internet Explorer, Microsoft Edge) optimiert. Um die einwandfreie Funktion und eine korrekte Darstellung zu gewährleisten, stellen Sie bitte sicher, dass Sie jeweils die aktuelle Version des entsprechenden Browsers verwenden. Der Internet Explorer wird ab der Version 11 unterstützt.\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section1\"],[7],[0,\"    \\n        \"],[6,\"h2\"],[7],[0,\"Was ist VarWatch?\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"VarWatch ist ein Werkzeug für akkreditierte Humangenetiker und Wissenschaftler, die im Rahmen Ihrer Tätigkeit mit genetischen Daten von Patienten arbeiten. Vermehrt werden durch die Hochdurchsatzsequenzierung genomische Varianten gefunden, bei denen ein Zusammenhang mit einem klinischen Phänotyp vermutet aber durch fehlende Beobachtungen in anderen Patienten nicht schlussendlich geklärt werden kann. VarWatch bietet hierfür nun eine unabhängige, nicht-kommerzielle Plattform an, auf der solche VUS (\"],[6,\"em\"],[7],[0,\"variants of unknown significance\"],[8],[0,\") registriert und mit externen Datenbanken und dem Variantenregister in VarWatch kontinuierlich abgeglichen werden können. Mögliche Übereinstimmungen zu anderen Fallbeschreibungen werden an die Variantenbesitzer übermittelt, um die Findung einer Diagnose für die betroffenen Patienten zu unterstützen.\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section2\"],[7],[0,\"    \\n        \"],[6,\"h2\"],[7],[0,\"Was ist VarWatch nicht?\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"VarWatch ist keine einfache „Liste“ von Varianten, die man frei durchsuchen oder herunterladen und analysieren kann (wie z.B. dbSNP oder ClinVar). Stattdessen implementieren wir ein „geben-und-nehmen“ Prinzip, um eine aktive Teilnahme an dem Register zu fördern. Konkret bedeutet dies, dass für die Nutzung von VarWatch ein tatsächlicher Fall mit einer oder mehreren VUS Varianten und Phänotyp-Beschreibung vorliegen muss. Dieser wird in Form einer Suchanfrage an den Service übermittelt und passende Treffer zu diesem Datensatz werden dem Nutzer regelmäßig mitgeteilt.  Auf diesem Wege wird jede Anfrage an den VarWatch Service gleichzeitig ein Eintrag in die „Watchliste“ und erhöht somit die Chance für alle Teilnehmer heute oder in der Zukunft eine Übereinstimmung zu finden.\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section3\"],[7],[0,\"    \\n        \"],[6,\"h2\"],[7],[0,\"Anmeldung\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"Die Nutzung von VarWatch erfordert eine Registrierung, wobei nur Individuen nicht aber Institutionen einen Zugang beantragen können. Voraussetzung für einen Zugang bei VarWatch ist, dass die Antragstellerin bzw. der Antragssteller im Bereich der Humangenetik tätig sind.\"],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Für die Anmeldung klicken sie bitte auf den Link „Register“ oben rechts auf der Webseite und füllen das Anmeldeformular vollständig aus. Ihre Daten werden anschließend in einem PDF Dokument zusammengestellt, welches Sie ausdrucken und unterschrieben an die im Dokument angegebene Adresse schicken müssen. Sobald ihre Informationen verifiziert wurden, wird Ihr Zugang mit dem von Ihnen gewählten Passwort freigeschaltet. Dies sollte in aller Regel nicht länger als 3-4 Tage dauern.\"],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Wir halten dieses Prozedere für sinnvoll, da VarWatch letztlich mit – wenn auch stark reduzierten und pseudonymisierten – Patientendaten arbeitet. Jeglicher Missbrauch muss daher nachvollziehbar und einer Person zuzuordnen sein.\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section4\"],[7],[0,\" \\n        \"],[6,\"h2\"],[7],[0,\"Nutzung von VarWatch\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"Dem Nutzer stehen drei Grundfunktionen des VarWatch Service zur Verfügung – das Einstellen neuer Varianten, die Abfrage des Status bereits eingestellter Varianten und Einsicht in die eigenen Benutzerdaten und Einstellungen.\"],[8],[0,\"\\n        \"],[6,\"figure\"],[7],[0,\"\\n          \"],[6,\"img\"],[9,\"src\",\"/assets/images/help1.png\"],[7],[8],[0,\"\\n          \"],[6,\"figcaption\"],[9,\"class\",\"small\"],[7],[6,\"strong\"],[7],[0,\"Grafik 1:\"],[8],[0,\" Die Startseite von VarWatch bietet eingeloggten Nutzer Zugriff auf die drei Grundfunktionen des Service an – neue Datensätze hochladen, den Status bereits eingestellter Datensätze abfragen und die Verwaltung der eigenen Benutzerdaten und Einstellungen. Ebenfalls werden dem Nutzer hier eine Übersicht zu den neusten Matches angezeigt.\"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"        \\n      \"],[6,\"div\"],[9,\"id\",\"section5\"],[7],[0,\"\\n        \"],[6,\"h2\"],[7],[0,\"Datensätze\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"Ein typischer Datensatz für VarWatch besteht aus einer kleinen Zahl von Kandidaten-Varianten (VUS) (≤ 50), die am Ende einer gründlichen automatischen (und ggf. manuellen) Filtrierung eines Genpanels oder einer Exom- oder bzw. Genomsequenzierung übriggeblieben sind. Diese Varianten müssen einen bioinformatisch nachweislichen Effekt auf die protein-kodierenden Komponenten eines Genes haben (z.B. ein frühzeitiges Stopkodon) und dürfen in noch keiner unserer externen Referenzdatenbanken bekannt sein (aktuell: ClinVar, HGMD).\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section6\"],[7],[0,\"\\n        \"],[6,\"h2\"],[7],[0,\"Varianten einstellen\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"Das Einstellen von neuen Varianten in die Watchliste erlaubt derzeit 3 Eingabeformate – VCF, HGVS und eine „direkte“ Eingabe der genomischen Koordinaten und des beobachteten Alleles. In allen Fällen müssen ein oder mehrere HPO Terme (\"],[6,\"em\"],[7],[0,\"Human Phenotype Ontology\"],[8],[0,\", human-phenotype-ontology.org) zur Beschreibung des Patienten angegeben werden.\"],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Der Vorgang einen neuen Datensatz (Patienten) anzulegen umfasst die folgenden Schritte:\"],[8],[0,\"\\n        \"],[6,\"ul\"],[9,\"class\",\"text-list\"],[7],[0,\"\\n          \"],[6,\"li\"],[7],[0,\"Angabe einer Beschreibung des Datensatzes (dies könnte eine interne Fall-ID des Diagnostikers sein, sollte aber datenschutz-technisch unbedenklich gewählt werden – d.h. keine Klarnamen o.ä.).\"],[8],[0,\"\\n          \"],[6,\"li\"],[7],[0,\"Das Alter, in welchem das Krankheitsbild zuerst auftrat – hier wird eine kategorische Beschreibung nach HPO verwendet.\"],[8],[0,\"\\n          \"],[6,\"li\"],[7],[0,\"Der beobachtete/vermutete Erbgang – ebenfalls basierend auf HPO Kategorien.\"],[8],[0,\"\\n          \"],[6,\"li\"],[7],[0,\"Ein oder mehrere Phänotypbeschreibungen nach HPO. Hierzu stellt das Interface einen einfachen Browser zur Verfügung, über welchen HPO Terme basierend auf Stichworten gefunden werden können. Jeder Term muss dann über den Knopf „Add selected HPO-Term“ der Liste hinzugefügt werden. Eine genauere Funktionsbeschreibung wird im nächsten Abschnitt vorgestellt. Alternativ können die HPO IDs (getrennt durch Leerzeichen) als Direkteingabe akzeptiert werden, wenn der Nutzer sie bereits auf einem anderen Weg bestimmt hat.\"],[8],[0,\"\\n          \"],[6,\"li\"],[7],[0,\"Als Eingabeformat stehen neben dem Hochladen von Dateien im VCF Format auch HGVS (ein oder mehrere durch Leerzeichen separierte Einträge) und eine Direkteingabe (ein oder mehrere Varianten) zur Verfügung.\"],[8],[0,\" \\n          \"],[6,\"li\"],[7],[0,\"Das Koordinatensystem auf welchem die Variante(n) basieren muss zwingend angegeben werden. Unterstützt werden die Assembly-Versionen GRCh38 (hg38) und GRCh37 (hg19). VarWatch basiert auf dem aktuellen Standard GRCh38 und wird Datensätze, die auf dem veralteten Standard GRCh37 basieren entsprechend auf GRCh38 projizieren. Dieser Vorgang wird erfahrungsgemäß bei >99% von Varianten erfolgreich sein. Dennoch empfehlen wir in diesem Fall dringend, dass Nutzer die auf diese Weise ermittelten Koordinaten überprüfen.\"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"Der Datensatz kann abschließend über den „Submit“ Knopf an VarWatch geschickt werden und wird nun auch auf der Status-Seite erscheinen. In aller Regel dauert die Bearbeitung eines neuen Datensatzes nur ein paar Minuten – wenn das System nicht gerade eine große Zahl von neuen Anfragen abarbeitet.\"],[8],[0,\"\\n        \"],[6,\"figure\"],[7],[0,\"\\n          \"],[6,\"img\"],[9,\"src\",\"/assets/images/help2.png\"],[7],[8],[0,\"\\n          \"],[6,\"figcaption\"],[9,\"class\",\"small\"],[7],[6,\"strong\"],[7],[0,\"Grafik 2:\"],[8],[0,\" Die Eingabemaske von VarWatch erlaubt neben der Übermittlung von Varianten in verschiedenen Formaten z.B. auch Angaben zu beobachteten Phänotypen nach dem HPO Standard, um relevantere Übereinstimmungen in der „watch list“ zu finden.\"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section7\"],[7],[0,\"\\n        \"],[6,\"h2\"],[7],[0,\"Der HPO Browser\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"Der HPO Browser steht als Werkzeug für die Navigation des zunehmend komplexen Vokabulars für systematische Phänotypisierung nach dem HPO Standard zur Verfügung. Die HPO ist in einer sogenannten Ontologie angelegt, einem vernetzten Baum von Eltern-Kind-Beziehungen, mit welcher sich neben unterschiedlichen Kategorien von Phänotypen auch feinere Abstufungen innerhalb dieser Kategorien abbilden lassen.\"],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Zur Navigation der HPO kann über die Suchmaske ein (englischer) Begriff eingegeben werden, zu welchem dann alle passenden Einträge in der HPO aufgelistet werden. Die Auswahl eines Terms zeigt dem Nutzer an, welche Eltern- und Kinder-Terme diesem Eintrag zugehörig sind. Die Beziehung zwischen Eltern- und Kindertermen ist hierbei durch eine Farbkodierung visualisiert und ihre Anzahl entsprechend gekennzeichnet. So ist es möglich, eine erste Auswahl ggf. nochmals zu verfeinern. Der als letztes ausgewählte HPO Term erscheint in der Auswahlliste und kann über den Knopf „Add selected HPO Term“ der Phänotypen-Liste des Datensatzes hinzugefügt werden.\"],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Hierbei ist zu beachten, dass es zwar wichtig ist eine ordentliche Phänotypisierung durchzuführen, um später möglichst relevante Übereinstimmungen zu finden. Jedoch kann VarWatch – bedingt durch die Netzwerkartige Struktur der HPO -  neben exakten Übereinstimmungen auch entferntere Ähnlichkeiten identifizieren, wodurch eine hundertprozentige Präzision in der Wahl der HPO Terme nicht zwingend nötig ist.\"],[8],[0,\"\\n        \"],[6,\"figure\"],[7],[0,\"\\n          \"],[6,\"img\"],[9,\"src\",\"/assets/images/help3.png\"],[7],[8],[0,\"\\n          \"],[6,\"figcaption\"],[9,\"class\",\"small\"],[7],[6,\"strong\"],[7],[0,\"Grafik 3:\"],[8],[0,\" Der HPO Browser erlaubt Nutzern eine einfache Navigation des Phänotypen Vokabulars.\"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section8\"],[7],[0,\"\\n        \"],[6,\"h2\"],[7],[0,\"HGVS Konvertierung\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"In der Bioinformatik hat sich in den letzten Jahren das VCF Format als ein weit verbreiteter Standard für die Reportierung von Varianten etabliert. Jedoch ist es besonders in der Diagnostik oft noch üblich beobachtete Varianten in der sogenannten (nicht sehr Informatik-freundlichen) HGVS Notation darzustellen. VarWatch akzeptiert natürlich Eingaben im HGVS Format und kann viele dieser Annotation ohne direkte Einflussnahme des Nutzers in ein internes (VCF-artiges) Datenformat umwandeln. Darüber hinaus bietet wir auf unserer Webseite aber auch ein kleines Konvertierungswerkzeug an. Hierdurch wird es Nutzern ermöglicht eine Liste von HGVS Varianten in das VCF Format zu überführen und ggf. anschließend auf Korrektheit zu überprüfen. Um zu dem HGVS Konvertierer zu gelangen, klicken Sie bitte oben rechts auf „HGVS Converter“. Geben Sie dann einfach eine Liste von HGVS Termen an und klicken Sie abschließend auf „Convert Terms“. Die übersetzten Varianten können dann als VCF exportiert werden („Export as VCF“).\"],[8],[0,\"\\n        \"],[6,\"figure\"],[7],[0,\"\\n          \"],[6,\"img\"],[9,\"src\",\"/assets/images/help4.png\"],[7],[8],[0,\"\\n          \"],[6,\"figcaption\"],[9,\"class\",\"small\"],[7],[6,\"strong\"],[7],[0,\"Grafik 4:\"],[8],[0,\" VarWatch bietet einen separaten Konvertierungsservice für Varianten im HGVS Format an, welcher neben dem Export im VCF Format auch die manuelle Validierung der Übersetzungen ermöglicht.\"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section9\"],[7],[0,\"\\n        \"],[6,\"h2\"],[7],[0,\"Status abfragen\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"Die Status Abfrage zeigt alle bereits angestellten Datensätze (=Patienten) mit ihrem aktuellen Status („submitted“ oder „observed“), einem Kommentar des VarWatch Systems über die letzte Aktivität und einem Zeitstempel an.\"],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Die Auswahl eines Datensatzes leitet den Nutzer auf eine Übersichtsseite weiter, auf welcher alle relevanten Informationen zu dem Datensatz sowie den darin enthaltenen Varianten aufgelistet wird. Hierbei werden Varianten, die unter aktiver Beobachtung und somit zum „matchen“ zur Verfügung stehen, getrennt von Varianten, die aus unterschiedlichen Gründen nicht für das „matchen“ in Frage kommen. Konkret ist dies der Fall, wenn die Variante bereits in externen Referenzdatenbanken bekannt ist („\"],[6,\"em\"],[7],[0,\"variant matched to beacon XXX\"],[8],[0,\"“) oder basierend auf dem EnsEMBL \"],[6,\"em\"],[7],[0,\"variant effect predictor\"],[8],[0,\" (\"],[6,\"em\"],[7],[0,\"VEP\"],[8],[0,\") keinen nachweislich störenden Effekt auf das Gen hat („\"],[6,\"em\"],[7],[0,\"no variant effect with high or moderate impact\"],[8],[0,\"“).\"],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Zu jeder akzeptierten Variante können detaillierte Informationen abgefragt werden, wie z.B. überlappende Gene und deren kanonische Transkripte inklusive der vermuteten Konsequenz der Variante auf dieses Transkript. Für Varianten mit ähnlichen Einträgen in VarWatch (angezeigt durch das kleine VarWatch Logo) oder HGMD (HGMD Logo) werden alle „Treffer“ aufgelistet mit Informationen dazu in welcher Datenbank der Treffer stattfand, wann er gefunden wurde, eine Ähnlichkeitsangabe aller beteiligten HPO Terme (Monarch Score – 100 = identische Phänotypen, 0 = keine Übereinstimmung der Phänotypen) und Informationen zu dem Match inklusive der externen ID (HGMD) bzw. der Kontaktinformation des „gematchten“ Nutzers (VarWatch).\"],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Der Anwender kann diese Informationen nun nutzen, um für jeden reportierten Match zu entscheiden, ob eine weiterführende Evaluierung sinnvoll ist. Für Treffer in der HGMD Datenbank kann die HGMD ID genutzt werden, um über einen bestehenden Zugang bei HGMD weitere Informationen zu der gematchten Variante zu finden (aus Lizenz-rechtlichen Gründen darf VarWatch diese Informationen nicht preisgeben). Für alle Matches gegen das VarWatch Inventar werden dem Nutzer direkt die Kontaktdaten des gematchten Nutzers angezeigt, so dass eine direkte Kontaktaufnahme stattfinden kann.\"],[8],[0,\"\\n        \"],[6,\"figure\"],[7],[0,\"\\n          \"],[6,\"img\"],[9,\"src\",\"/assets/images/help5.png\"],[7],[8],[0,\"\\n          \"],[6,\"figcaption\"],[9,\"class\",\"small\"],[7],[6,\"strong\"],[7],[0,\"Grafik 5:\"],[8],[0,\" Datensatz Übersicht mit beispielhafter Variantenannotation und Statusanzeige.\"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"section10\"],[9,\"class\",\"fillup\"],[7],[0,\"\\n        \"],[6,\"h2\"],[7],[0,\"Nutzereinstellungen\"],[8],[0,\"\\n        \"],[6,\"p\"],[7],[0,\"Es besteht die Möglichkeit für Nutzer ihre Personenbezogenen Informationen einzusehen. Außerdem bietet VarWatch ein konfigurierbares „Reporting“ an, über welches Nutzer entscheiden können wie oft Sie über Email von dem System zu neuen Matches informiert werden wollen (täglich, wöchentlich, monatlich oder nie). Auch kann hier auch ein neues Passwort gesetzt werden.\"],[8],[0,\"\\n        \"],[6,\"figure\"],[7],[0,\"\\n          \"],[6,\"img\"],[9,\"src\",\"/assets/images/help6.png\"],[7],[8],[0,\"\\n          \"],[6,\"figcaption\"],[9,\"class\",\"small\"],[7],[6,\"strong\"],[7],[0,\"Grafik 6:\"],[8],[0,\" VarWatch Nutzer können über das Nutzermenü sowohl ihre eignen Daten einsehen, sowie ein Reportierungsintervall für neue Matches wählen als auch ein neues Passwort setzen.\"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \\n    \"],[6,\"div\"],[9,\"class\",\"col-md-3 hidden-xs hidden-sm\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"id\",\"myAffix\"],[9,\"data-spy\",\"affix\"],[9,\"data-offset-top\",\"30\"],[9,\"data-offset-bottom\",\"30\"],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"id\",\"myScrollspy\"],[7],[0,\"\\n          \"],[6,\"ul\"],[9,\"class\",\"nav nav-pills nav-stacked\"],[7],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section0\"],[7],[0,\"Unterstützte Browser\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section1\"],[7],[0,\"Was ist VarWatch?\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section2\"],[7],[0,\"Was ist VarWatch nicht?\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section3\"],[7],[0,\"Anmeldung\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section4\"],[7],[0,\"Nutzung von VarWatch\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section5\"],[7],[0,\"Datensätze\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section6\"],[7],[0,\"Varianten einstellen\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section7\"],[7],[0,\"Der HPO Browser\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section8\"],[7],[0,\"HGVS Konvertierung\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section9\"],[7],[0,\"Status abfragen\"],[8],[8],[0,\"\\n            \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#section10\"],[7],[0,\"Nutzereinstellungen\"],[8],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n          \"],[6,\"ul\"],[9,\"class\",\"nav nav-pills nav-stacked\"],[7],[0,\"\\n          \"],[6,\"li\"],[7],[6,\"a\"],[9,\"href\",\"#top\"],[7],[0,\"nach oben\"],[8],[8],[0,\"\\n\\n          \"],[2,\"\\n          <li class=\\\"dropdown\\\">\\n            <a class=\\\"dropdown-toggle\\\" data-toggle=\\\"dropdown\\\" href=\\\"#\\\">Section 4 <span class=\\\"caret\\\"></span></a>\\n            <ul class=\\\"dropdown-menu\\\">\\n              <li><a href=\\\"#section41\\\">Varianten  einstellen</a></li>\\n              <li><a href=\\\"#section42\\\">Section 4-2</a></li>\\n            </ul>\\n          </li>\\n          \"],[0,\"\\n          \\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n\\n  \"],[8],[0,\"\\n\\n\"],[8],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/help-page.hbs" } });
});
define("varwatch/templates/components/hgvs-list", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "Bdv9soMx", "block": "{\"symbols\":[\"variant\",\"index\"],\"statements\":[[4,\"if\",[[20,[\"hgvsVariants\"]]],null,{\"statements\":[[0,\"  \"],[6,\"table\"],[9,\"class\",\"table table-condensed no-bottom-margin\"],[7],[0,\"\\n    \"],[6,\"thead\"],[7],[0,\"\\n      \"],[6,\"tr\"],[7],[0,\"\\n        \"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"#\"],[8],[0,\"\\n        \"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"HGVS Term\"],[8],[0,\"\\n        \"],[2,\" <th class=\\\"text-center\\\">Reference Assembly</th> \"],[0,\"\\n        \"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Remove\"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"hgvsVariants\"]]],null,{\"statements\":[[0,\"        \"],[6,\"tr\"],[7],[0,\"\\n          \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[25,\"plusone\",[[19,2,[]]],null],false],[8],[0,\"\\n          \"],[6,\"td\"],[10,\"class\",[26,[\"text-center \",[25,\"if\",[[19,1,[\"errorString\"]],\"error-red\"],null]]]],[7],[0,\"\\n            \"],[1,[19,1,[\"hgvs\"]],false],[0,\" \\n\"],[4,\"if\",[[19,1,[\"errorString\"]]],null,{\"statements\":[[0,\"              \"],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-exclamation-sign\"],[9,\"aria-hidden\",\"true\"],[9,\"data-toggle\",\"tooltip\"],[10,\"title\",[19,1,[\"errorString\"]],null],[7],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"          \"],[8],[0,\"\\n          \"],[2,\" <td class=\\\"text-center\\\">{{variant.assembly}}</td> \"],[0,\"\\n          \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default btn-xs\"],[9,\"aria-label\",\"Remove\"],[3,\"action\",[[19,0,[]],[20,[\"removeVariant\"]],[19,2,[]]]],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-remove\"],[9,\"aria-hidden\",\"true\"],[7],[8],[8],[8],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[1,2]},null],[0,\"    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"  \"],[6,\"div\"],[9,\"class\",\"panel-body form-panel\"],[9,\"id\",\"hgvsvariants\"],[7],[0,\"\\n    no Variants\\n  \"],[8],[0,\"\\n\"]],\"parameters\":[]}]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/hgvs-list.hbs" } });
});
define("varwatch/templates/components/home-links", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "stmCW5Nb", "block": "{\"symbols\":[],\"statements\":[[6,\"div\"],[9,\"class\",\"indexrow\"],[7],[0,\"\\n\"],[4,\"link-to\",[\"submit\"],[[\"class\",\"alt\"],[\"btn btn-default image-button\",\"Submit\"]],{\"statements\":[[0,\"    \"],[6,\"img\"],[9,\"src\",\"/assets/images/vw_icon_upload.png\"],[9,\"width\",\"187\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"bottom\"],[9,\"title\",\"Submit Data\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"link-to\",[\"datasets\"],[[\"class\",\"alt\"],[\"btn btn-default image-button\",\"Datasets\"]],{\"statements\":[[0,\"    \"],[6,\"img\"],[9,\"src\",\"/assets/images/vw_icon_dataset.png\"],[9,\"width\",\"187\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"bottom\"],[9,\"title\",\"View Results\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"link-to\",[\"userinfo\"],[[\"class\",\"alt\"],[\"btn btn-default image-button\",\"User Information\"]],{\"statements\":[[0,\"    \"],[6,\"img\"],[9,\"src\",\"/assets/images/vw_icon_userinfo.png\"],[9,\"width\",\"187\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"bottom\"],[9,\"title\",\"User Information\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]},null],[8],[0,\" \\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/home-links.hbs" } });
});
define("varwatch/templates/components/hpo-browser", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "i4GtPWRh", "block": "{\"symbols\":[\"child\",\"pred\",\"sibling\",\"pred\",\"parent\",\"index\",\"pred\",\"index\",\"res\"],\"statements\":[[4,\"if\",[[20,[\"hpo\",\"dataloaded\"]]],null,{\"statements\":[[6,\"div\"],[9,\"class\",\"panel panel-default no-bottom-margin\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"panel-heading vpadding-5px panel-heading-hover\"],[3,\"action\",[[19,0,[]],\"toggleShowBrowser\"]],[7],[0,\"\\n    \"],[6,\"h4\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"HPO Browser\"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[4,\"liquid-if\",[[20,[\"showBrowser\"]]],null,{\"statements\":[[0,\"  \"],[6,\"div\"],[9,\"class\",\"panel-body no-bottom-padding padding-15px\"],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel panel-default bottom-margin-15px\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"input-group no-bottom-margin panel-button-fullwidth top-margin-n1px\"],[7],[0,\"\\n            \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control no-bl-radius\",\"hposearch\",\"Enter Search Term\",[20,[\"searchterm\"]]]]],false],[0,\"\\n            \"],[6,\"span\"],[9,\"class\",\"input-group-btn\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default no-br-radius\"],[3,\"action\",[[19,0,[]],\"searchHpos\",[20,[\"searchterm\"]]]],[7],[6,\"nobr\"],[7],[0,\"Search HPO\"],[8],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-heading no-vert-padding no-top-radius\"],[7],[0,\"\\n            \"],[6,\"h4\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"\\n              Results\"],[4,\"if\",[[20,[\"searchResults\"]]],null,{\"statements\":[[0,\" (\"],[1,[20,[\"searchResults\",\"length\"]],false],[0,\")\"]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-body height-limit small-padding\"],[7],[0,\"\\n\"],[4,\"if\",[[20,[\"searchResults\"]]],null,{\"statements\":[[0,\"              \"],[6,\"ul\"],[9,\"class\",\"list-unstyled no-bottom-margin\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"searchResults\"]]],null,{\"statements\":[[0,\"                  \"],[6,\"li\"],[7],[0,\"\\n\"],[4,\"if\",[[19,9,[\"isAltOf\"]]],null,{\"statements\":[[0,\"                      \"],[1,[19,9,[\"term\"]],false],[0,\": Alternative Term for \"],[6,\"a\"],[9,\"href\",\"#\"],[3,\"action\",[[19,0,[]],\"hpoChanged\",[19,9,[\"isAltOf\"]]]],[7],[1,[19,9,[\"isAltOf\"]],false],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                      \"],[6,\"a\"],[9,\"href\",\"#\"],[3,\"action\",[[19,0,[]],\"hpoChanged\",[19,9,[\"term\"]]]],[7],[1,[19,9,[\"term\"]],false],[8],[0,\": \"],[1,[19,9,[\"desc\"]],false],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[19,9,[\"succ\",\"length\"]],\"succ\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-down\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[19,9,[\"succ\",\"length\"]],false],[8],[8],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[19,9,[\"pred\",\"length\"]],\"pred\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-up\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[19,9,[\"pred\",\"length\"]],false],[8],[8],[0,\"\\n\"]],\"parameters\":[]}],[0,\"                  \"],[8],[0,\"\\n\"]],\"parameters\":[9]},null],[0,\"              \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"              -\\n\"]],\"parameters\":[]}],[0,\"          \"],[8],[0,\"\\n\"],[4,\"if\",[[20,[\"searchResults\"]]],null,{\"statements\":[[0,\"            \"],[6,\"div\"],[9,\"class\",\"panel-button-fullwidth\"],[7],[0,\"\\n              \"],[6,\"div\"],[9,\"class\",\"btn-group btn-group-justified neg-margin\"],[9,\"role\",\"group\"],[9,\"aria-label\",\"...\"],[7],[0,\"\\n                \"],[6,\"div\"],[9,\"class\",\"btn-group\"],[9,\"role\",\"group\"],[7],[0,\"\\n                  \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default panel-button no-vert-padding\"],[3,\"action\",[[19,0,[]],\"clearSearchResults\"]],[7],[0,\"Clear\"],[8],[0,\"\\n                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"        \"],[8],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel panel-default bottom-margin-15px\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-heading no-vert-padding\"],[7],[0,\"\\n        \"],[6,\"h4\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Selected HPO Term\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-body height-limit small-padding\"],[7],[0,\"\\n        \"],[6,\"p\"],[9,\"class\",\"no-bottom-margin\"],[7],[6,\"span\"],[9,\"class\",\"bar bg-cyan\"],[7],[8],[6,\"strong\"],[7],[1,[20,[\"selectedHpo\",\"term\"]],false],[0,\": \"],[1,[20,[\"selectedHpo\",\"desc\"]],false],[8],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[20,[\"selectedHpo\",\"succ\",\"length\"]],\"succ\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-down\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[20,[\"selectedHpo\",\"succ\",\"length\"]],false],[8],[8],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[20,[\"selectedHpo\",\"pred\",\"length\"]],\"pred\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-up\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[20,[\"selectedHpo\",\"pred\",\"length\"]],false],[8],[4,\"each\",[[20,[\"selectedHpo\",\"pred\"]]],null,{\"statements\":[[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"add\",[[19,7,[]],\" \",[25,\"get-hpo-desc\",[[19,7,[]]],null]],null],null],[10,\"class\",[25,\"add\",[\"bar \",[25,\"getbarclass\",[[19,8,[]]],null]],null],null],[7],[8]],\"parameters\":[7,8]},null],[8],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-button-fullwidth\"],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"btn-group btn-group-justified neg-margin\"],[9,\"role\",\"group\"],[9,\"aria-label\",\"...\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"btn-group\"],[9,\"role\",\"group\"],[7],[0,\"\\n            \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default panel-button vert-padding-3px \"],[3,\"action\",[[19,0,[]],[20,[\"addHpo\"]],[20,[\"selectedHpo\",\"term\"]]]],[7],[0,\"Add HPO Term to Phenotypes\"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel panel-default bottom-margin-15px\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-heading no-vert-padding\"],[7],[0,\"\\n            \"],[6,\"h4\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Parent Terms\"],[4,\"if\",[[20,[\"hpoParents\"]]],null,{\"statements\":[[0,\" (\"],[1,[20,[\"hpoParents\",\"length\"]],false],[0,\")\"]],\"parameters\":[]},null],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-body height-limit small-padding\"],[7],[0,\"\\n\"],[4,\"if\",[[20,[\"hpoParents\"]]],null,{\"statements\":[[0,\"              \"],[6,\"ul\"],[9,\"class\",\"list-unstyled no-bottom-margin \"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"hpoParents\"]]],null,{\"statements\":[[0,\"                  \"],[6,\"li\"],[7],[0,\"\\n                    \"],[6,\"span\"],[10,\"class\",[25,\"add\",[\"bar \",[25,\"getbarclass\",[[19,6,[]]],null]],null],null],[7],[8],[6,\"a\"],[9,\"href\",\"#\"],[3,\"action\",[[19,0,[]],\"hpoChanged\",[19,5,[\"term\"]]]],[7],[1,[19,5,[\"term\"]],false],[8],[0,\": \"],[1,[19,5,[\"desc\"]],false],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[19,5,[\"succ\",\"length\"]],\"succ\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-down\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[19,5,[\"succ\",\"length\"]],false],[8],[8],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[19,5,[\"pred\",\"length\"]],\"pred\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-up\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[19,5,[\"pred\",\"length\"]],false],[8],[8],[0,\"\\n                  \"],[8],[0,\"\\n\"]],\"parameters\":[5,6]},null],[0,\"              \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"              -\\n\"]],\"parameters\":[]}],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel panel-default bottom-margin-15px\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-heading no-vert-padding\"],[7],[0,\"\\n            \"],[6,\"h4\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Sibling Terms\"],[4,\"if\",[[20,[\"hpoSiblings\"]]],null,{\"statements\":[[0,\" (\"],[1,[20,[\"hpoSiblings\",\"length\"]],false],[0,\")\"]],\"parameters\":[]},null],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-body height-limit small-padding\"],[7],[0,\"\\n\"],[4,\"if\",[[20,[\"hpoSiblings\"]]],null,{\"statements\":[[0,\"              \"],[6,\"ul\"],[9,\"class\",\"list-unstyled no-bottom-margin\"],[7],[0,\"\\n                \"],[2,\"\\n                <li><p class=\\\"selected-color no-bottom-margin\\\">{{selectedHpo.term}}: {{selectedHpo.desc}}<span class=\\\"badge hpo-badge\\\">{{selectedHpo.succ.length}}</span></p></li>\\n                \"],[0,\"\\n\"],[4,\"each\",[[20,[\"hpoSiblings\"]]],null,{\"statements\":[[0,\"                  \"],[6,\"li\"],[7],[0,\"\\n                    \"],[6,\"a\"],[9,\"href\",\"#\"],[3,\"action\",[[19,0,[]],\"hpoChanged\",[19,3,[\"term\"]]]],[7],[1,[19,3,[\"term\"]],false],[8],[0,\": \"],[1,[19,3,[\"desc\"]],false],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[19,3,[\"succ\",\"length\"]],\"succ\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-down\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[19,3,[\"succ\",\"length\"]],false],[8],[8],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[19,3,[\"pred\",\"length\"]],\"pred\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-up\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[19,3,[\"pred\",\"length\"]],false],[8],[0,\" \"],[4,\"each\",[[19,3,[\"pred\"]]],null,{\"statements\":[[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"add\",[[19,4,[]],\" \",[25,\"get-hpo-desc\",[[19,4,[]]],null]],null],null],[10,\"class\",[25,\"add\",[\"bar \",[25,\"getbarclass\",[[25,\"getpredindex\",[[19,4,[]],[20,[\"hpoParents\"]]],null]],null]],null],null],[7],[8]],\"parameters\":[4]},null],[8],[0,\"\\n                  \"],[8],[0,\"\\n\"]],\"parameters\":[3]},null],[0,\"              \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"              -\\n\"]],\"parameters\":[]}],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel panel-default no-bottom-margin\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-heading no-vert-padding\"],[7],[0,\"\\n            \"],[6,\"h4\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Child Terms (\"],[1,[20,[\"hpoChildren\",\"length\"]],false],[0,\")\"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-body small-padding height-limit\"],[7],[0,\"\\n\"],[4,\"if\",[[20,[\"hpoChildren\"]]],null,{\"statements\":[[0,\"              \"],[6,\"ul\"],[9,\"class\",\"list-unstyled no-bottom-margin\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"hpoChildren\"]]],null,{\"statements\":[[0,\"                  \"],[6,\"li\"],[7],[0,\"\\n                    \"],[6,\"a\"],[9,\"href\",\"#\"],[3,\"action\",[[19,0,[]],\"hpoChanged\",[19,1,[\"term\"]]]],[7],[1,[19,1,[\"term\"]],false],[8],[0,\": \"],[1,[19,1,[\"desc\"]],false],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[19,1,[\"succ\",\"length\"]],\"succ\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-down\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[19,1,[\"succ\",\"length\"]],false],[8],[8],[6,\"span\"],[9,\"class\",\"badge hpo-badge\"],[7],[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"get-kin-tooltip-text\",[[19,1,[\"pred\",\"length\"]],\"pred\"],null],null],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-arrow-up\"],[9,\"aria-hidden\",\"true\"],[7],[8],[1,[19,1,[\"pred\",\"length\"]],false],[8],[0,\" \"],[4,\"each\",[[19,1,[\"pred\"]]],null,{\"statements\":[[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[25,\"add\",[[19,2,[]],\" \",[25,\"get-hpo-desc\",[[19,2,[]]],null]],null],null],[10,\"class\",[25,\"add\",[\"bar \",[25,\"getchildbarclass\",[[19,2,[]],[20,[\"selectedHpo\",\"term\"]]],null]],null],null],[7],[8]],\"parameters\":[2]},null],[8],[0,\"\\n                  \"],[8],[0,\"\\n\"]],\"parameters\":[1]},null],[0,\"              \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"              -\\n\"]],\"parameters\":[]}],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[6,\"p\"],[9,\"class\",\"text-right no-bottom-margin\"],[7],[6,\"small\"],[7],[0,\"Human Phenotype Ontology release: \"],[1,[20,[\"hpo\",\"hpoVersion\"]],false],[8],[8],[0,\"\\n  \"],[8],[0,\"\\n  \"],[2,\"\\n  <div class=\\\"panel-button-fullwidth\\\">\\n    <div class=\\\"btn-group btn-group-justified neg-margin\\\" role=\\\"group\\\" aria-label=\\\"...\\\">\\n      <div class=\\\"btn-group\\\" role=\\\"group\\\">\\n        <button type=\\\"button\\\" class=\\\"btn btn-default panel-button\\\" {{action addHpo selectedHpo.term}}>Add Term</button>\\n      </div>\\n    </div>\\n  </div>\\n  \"],[0,\"\\n\"]],\"parameters\":[]},null],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"  Waiting for HPO Service to be ready...\\n\"]],\"parameters\":[]}]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/hpo-browser.hbs" } });
});
define("varwatch/templates/components/match-type", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "yY8NUqMw", "block": "{\"symbols\":[],\"statements\":[[6,\"span\"],[9,\"data-toggle\",\"tooltip\"],[9,\"data-placement\",\"top\"],[10,\"title\",[18,\"matchTypeDescription\"],null],[7],[1,[18,\"matchType\"],false],[8]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/match-type.hbs" } });
});
define("varwatch/templates/components/modal-target", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "Y1VHi/pA", "block": "{\"symbols\":[\"modal\",\"currentModal\"],\"statements\":[[4,\"from-elsewhere\",null,[[\"name\"],[\"modal\"]],{\"statements\":[[4,\"liquid-bind\",[[19,1,[]]],[[\"containerless\",\"use\"],[true,[20,[\"modalAnimation\"]]]],{\"statements\":[[4,\"if\",[[19,2,[]]],null,{\"statements\":[[0,\"      \"],[6,\"div\"],[9,\"class\",\"modal-background\"],[7],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"modal-container\"],[7],[0,\"\\n        \"],[6,\"div\"],[10,\"class\",[26,[[25,\"if\",[[19,2,[\"isError\"]],\"modal-dialog error-dialog\",[25,\"if\",[[19,2,[\"isSuccess\"]],\"modal-dialog success-dialog\",[25,\"if\",[[19,2,[\"isWarning\"]],\"modal-dialog warning-dialog\",\"modal-dialog\"],null]],null]],null]]]],[9,\"role\",\"document\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"modal-content\"],[7],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"modal-header\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"close\"],[9,\"data-dismiss\",\"modal\"],[9,\"aria-label\",\"Close\"],[10,\"onclick\",[25,\"action\",[[19,0,[]],[19,2,[\"closeClick\"]]],null],null],[7],[6,\"span\"],[9,\"aria-hidden\",\"true\"],[7],[0,\"×\"],[8],[8],[0,\"\\n              \"],[6,\"h4\"],[9,\"class\",\"modal-title\"],[9,\"id\",\"loginModalLabel\"],[7],[1,[19,2,[\"title\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"modal-body\"],[7],[0,\"\\n              \"],[1,[19,2,[\"message\"]],true],[0,\"\\n            \"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"modal-footer\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[9,\"data-dismiss\",\"modal\"],[3,\"action\",[[19,0,[]],[19,2,[\"closeClick\"]]]],[7],[0,\"Ok\"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n\"]],\"parameters\":[]},null]],\"parameters\":[2]},null]],\"parameters\":[1]},null]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/modal-target.hbs" } });
});
define("varwatch/templates/components/registration-dialog", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "7H4OefJ+", "block": "{\"symbols\":[],\"statements\":[[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n    \"],[6,\"h1\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Register\"],[8],[0,\"\\n  \"],[8],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n    \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[9,\"id\",\"myform\"],[3,\"action\",[[19,0,[]],\"registerUser\"],[[\"on\"],[\"submit\"]]],[7],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"firstNameNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"firstName\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Given Name\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\",\"aria-describedby\"],[\"form-control\",\"firstName\",\"Enter Given Name\",[20,[\"firstName\"]],\"givenNameHelpBlock\"]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"firstNameNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"givenNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter your given name.\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"lastNameNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"lastName\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Family Name\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\",\"aria-describedby\"],[\"form-control\",\"lastName\",\"Enter Family Name\",[20,[\"lastName\"]],\"familyNameHelpBlock\"]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"lastNameNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"familyNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter your family name.\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"institutionNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"institution\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Institution\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\",\"aria-describedby\"],[\"form-control\",\"institution\",\"Enter Institution\",[20,[\"institution\"]],\"institutionHelpBlock\"]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"institutionNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter the name of your institution\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"      \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n        \"],[6,\"label\"],[9,\"for\",\"phone\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Phone (optional)\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"phone\",\"Enter Phone\",[20,[\"phone\"]]]]],false],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[25,\"or\",[[20,[\"emailExists\"]],[20,[\"emailNotOk\"]]],null],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"mail\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"eMail\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\",\"aria-describedby\"],[\"form-control\",\"mail\",\"Enter eMail\",[20,[\"mail\"]],\"emailHelpBlock emailExistsHelpBlock\"]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"emailNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"emailHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter a valid email address\"],[8]],\"parameters\":[]},null],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"emailExists\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"emailExistsHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"eMail address already registered\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"emailsDontMatch\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"mail\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"eMail again\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\",\"aria-describedby\"],[\"form-control\",\"mailrepeat\",\"Repeat eMail\",[20,[\"mailrepeat\"]],\"emailsDontMatchHelpBlock\"]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"emailsDontMatch\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"emailsDontMatchHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"eMail addresses don't match\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"passwordNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"password\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Password\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"type\",\"value\",\"aria-describedby\"],[\"form-control\",\"password\",\"Enter Password\",\"password\",[20,[\"password\"]],\"passwordHelpBlock\"]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"passwordNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"passwordHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Password must not be empty\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"passrepeatNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"passrepeat\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Password again\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"type\",\"value\",\"aria-describedby\"],[\"form-control\",\"passrepeat\",\"Repeat Password\",\"password\",[20,[\"passrepeat\"]],\"passrepeatHelpBlock\"]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"passrepeatNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"passwordHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Passwords do not match\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"addressNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"address\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Address\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"address\",\"Enter Address\",[20,[\"address\"]]]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"addressNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter your address\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"postalCodeNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"postalCode\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Postal Code\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"postalCode\",\"Enter Postal Code\",[20,[\"postalCode\"]]]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"postalCodeNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter the postal code of your city\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"cityNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"city\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"City\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"city\",\"Enter City\",[20,[\"city\"]]]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"cityNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter the name of your city\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"countryNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"        \"],[6,\"label\"],[9,\"for\",\"country\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Country\"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n          \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"country\",\"Enter Country\",[20,[\"country\"]]]]],false],[0,\"\\n          \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"countryNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter the name of your country\"],[8]],\"parameters\":[]},null],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"      \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n          \"],[6,\"button\"],[9,\"type\",\"submit\"],[9,\"class\",\"btn btn-default\"],[7],[0,\"Register\"],[8],[0,\"\\n          \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"resetForm\"]],[7],[0,\"Reset\"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[8],[0,\"\\n\\n\"],[4,\"if\",[[20,[\"errorMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isError\"],[\"Registration failed!\",[20,[\"errorMessage\"]],[25,\"action\",[[19,0,[]],\"closeModalDialog\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"\\n\"],[4,\"if\",[[20,[\"successMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isSuccess\"],[\"Registration successful!\",[20,[\"successMessage\"]],[25,\"action\",[[19,0,[]],\"closeModalDialog\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/registration-dialog.hbs" } });
});
define("varwatch/templates/components/row-link-to", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "aaiwsOH2", "block": "{\"symbols\":[\"&default\"],\"statements\":[[11,1],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/row-link-to.hbs" } });
});
define("varwatch/templates/components/variant-line", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "u23EF220", "block": "{\"symbols\":[\"vh\",\"index\",\"term\",\"gene\",\"consequence\",\"cons\",\"trid\",\"pathway\",\"family\",\"gene\"],\"statements\":[[6,\"tr\"],[9,\"class\",\"hoverrow\"],[3,\"action\",[[19,0,[]],\"toggleDetails\"]],[7],[6,\"td\"],[7],[1,[25,\"plusone\",[[20,[\"index\"]]],null],false],[8],[6,\"td\"],[7],[1,[20,[\"variant\",\"rawData\"]],false],[8],[6,\"td\"],[7],[1,[20,[\"variant\",\"chromosomeName\"]],false],[8],[6,\"td\"],[7],[1,[20,[\"variant\",\"position\"]],false],[8],[6,\"td\"],[7],[1,[20,[\"variant\",\"referenceBase\"]],false],[8],[6,\"td\"],[7],[1,[20,[\"variant\",\"alternateBase\"]],false],[8],[6,\"td\"],[7],[1,[25,\"gen-variant-status\",[[20,[\"variant\",\"variantstatus\",\"status\"]],[20,[\"variant\",\"varianthistory\",\"length\"]]],null],false],[8],[6,\"td\"],[7],[1,[20,[\"variant\",\"variantstatus\",\"localedatetime\"]],false],[8],[8],[0,\"\\n  \"],[6,\"tr\"],[7],[0,\"\\n    \"],[6,\"td\"],[9,\"colspan\",\"8\"],[9,\"class\",\"no-top-border no-vert-padding\"],[7],[0,\"\\n\"],[4,\"liquid-if\",[[20,[\"showDetails\"]]],null,{\"statements\":[[0,\"      \"],[6,\"div\"],[9,\"class\",\"panel panel-info vmargin-5px lmargin-5px\"],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel-body no-vert-padding\"],[7],[0,\"\\n          \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[7],[0,\"\\n\"],[4,\"if\",[[20,[\"variant\",\"genes\"]]],null,{\"statements\":[[0,\"              \"],[6,\"div\"],[9,\"class\",\"form-group no-bottom-margin\"],[7],[0,\"\\n                \"],[6,\"label\"],[9,\"for\",\"preName\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Genes\"],[8],[0,\"\\n                \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n                  \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"variant\",\"genes\"]]],null,{\"statements\":[[0,\"                      \"],[6,\"a\"],[10,\"href\",[25,\"gen-gene-link\",[[19,10,[\"identifier\"]]],null],null],[9,\"target\",\"_blank\"],[7],[1,[19,10,[\"identifier\"]],false],[8],[4,\"if\",[[19,10,[\"ensembl\",\"description\"]]],null,{\"statements\":[[0,\" \"],[1,[25,\"ensembl-data\",null,[[\"ensembl\"],[[19,10,[\"ensembl\"]]]]],false]],\"parameters\":[]},null],[4,\"if\",[[25,\"neq\",[[19,10,[]],[20,[\"variant\",\"genes\",\"lastObject\"]]],null]],null,{\"statements\":[[0,\", \"]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[10]},null],[0,\"                  \"],[8],[0,\"\\n                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"\\n\"],[4,\"if\",[[20,[\"variant\",\"families\"]]],null,{\"statements\":[[0,\"              \"],[6,\"div\"],[9,\"class\",\"form-group no-bottom-margin\"],[7],[0,\"\\n                \"],[6,\"label\"],[9,\"for\",\"preName\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Families\"],[8],[0,\"\\n                \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n                  \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"variant\",\"families\"]]],null,{\"statements\":[[0,\"                      \"],[6,\"a\"],[10,\"href\",[25,\"gen-family-link\",[[19,9,[\"identifier\"]]],null],null],[9,\"target\",\"_blank\"],[7],[1,[19,9,[\"identifier\"]],false],[8],[0,\" \"],[1,[19,9,[\"capitalizedDescription\"]],false],[4,\"if\",[[25,\"neq\",[[19,9,[]],[20,[\"variant\",\"families\",\"lastObject\"]]],null]],null,{\"statements\":[[0,\", \"]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[9]},null],[0,\"                  \"],[8],[0,\"\\n                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"if\",[[20,[\"variant\",\"pathways\"]]],null,{\"statements\":[[0,\"              \"],[6,\"div\"],[9,\"class\",\"form-group no-bottom-margin\"],[7],[0,\"\\n                \"],[6,\"label\"],[9,\"for\",\"preName\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Pathways\"],[8],[0,\"\\n                \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n                  \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"variant\",\"pathways\"]]],null,{\"statements\":[[0,\"                      \"],[6,\"a\"],[10,\"href\",[25,\"gen-pathway-link\",[[19,8,[\"identifier\"]]],null],null],[9,\"target\",\"_blank\"],[7],[1,[25,\"strip-pathway\",[[19,8,[\"identifier\"]]],null],false],[8],[0,\" \"],[1,[19,8,[\"kegg\",\"description\"]],false],[4,\"if\",[[25,\"neq\",[[19,8,[]],[20,[\"variant\",\"pathways\",\"lastObject\"]]],null]],null,{\"statements\":[[0,\", \"]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[8]},null],[0,\"                  \"],[8],[0,\"\\n                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel-heading panel-heading-middle\"],[7],[0,\"Ensembl Transcript Consequences\"],[8],[0,\"\\n\"],[4,\"if\",[[20,[\"variant\",\"ensemblData\",\"isPending\"]]],null,{\"statements\":[[0,\"          \"],[6,\"div\"],[9,\"class\",\"panel-body vpadding-5px\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"no-bottom-margin\"],[7],[0,\"Contacting Ensembl Rest Service. Please wait...\"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[20,[\"variant\",\"ensemblData\",\"isRejected\"]]],null,{\"statements\":[[0,\"          \"],[6,\"div\"],[9,\"class\",\"panel-body vpadding-5px\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"no-bottom-margin\"],[7],[0,\"Ensembl Rest Service currently unreachable. Please try again later.\"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"          \"],[6,\"table\"],[9,\"class\",\"table table-condensed ensembl-table\"],[7],[0,\"\\n            \"],[6,\"thead\"],[7],[0,\"\\n              \"],[6,\"tr\"],[7],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Gene ID\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Gene Symbol\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Transcript ID\"],[8],[2,\"<th class='text-center'>Biotype</th>\"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"RefSeq Transcript IDs\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"cDNA Start\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"cDNA End\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Consequence Terms \"],[6,\"a\"],[9,\"target\",\"_blank\"],[9,\"href\",\"http://www.ensembl.org/info/genome/variation/predicted_data.html#consequences\"],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-new-window\"],[9,\"aria-hidden\",\"true\"],[7],[8],[8],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Impact\"],[8],[8],[0,\"\\n            \"],[8],[0,\"\\n            \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"variant\",\"ensemblData\",\"firstObject\",\"transcript_consequences\"]]],null,{\"statements\":[[4,\"if\",[[25,\"and\",[[19,5,[\"canonical\"]],[25,\"or\",[[25,\"eq\",[[19,5,[\"impact\"]],\"MODERATE\"],null],[25,\"eq\",[[19,5,[\"impact\"]],\"HIGH\"],null]],null]],null]],null,{\"statements\":[[0,\"                  \"],[6,\"tr\"],[7],[0,\"\\n                    \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,5,[\"gene_id\"]],false],[8],[0,\"\\n                    \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,5,[\"gene_symbol\"]],false],[8],[0,\"\\n                    \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,5,[\"transcript_id\"]],false],[8],[0,\"\\n                    \"],[2,\"\\n                    <td class='text-center'>{{consequence.biotype}}</td>\\n                    \"],[0,\"\\n                    \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,5,[\"refseq_transcript_ids\"]]],null,{\"statements\":[[0,\"                        \"],[4,\"each\",[[19,5,[\"refseq_transcript_ids\"]]],null,{\"statements\":[[1,[19,7,[]],false],[4,\"if\",[[25,\"neq\",[[19,7,[]],[19,5,[\"refseq_transcript_ids\",\"lastObject\"]]],null]],null,{\"statements\":[[0,\", \"]],\"parameters\":[]},null]],\"parameters\":[7]},null],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                        -\\n\"]],\"parameters\":[]}],[0,\"                    \"],[8],[0,\"\\n                    \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,5,[\"cdna_start\"]]],null,{\"statements\":[[0,\"                        \"],[1,[19,5,[\"cdna_start\"]],false],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                        -\\n\"]],\"parameters\":[]}],[0,\"                    \"],[8],[0,\"\\n                    \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,5,[\"cdna_end\"]]],null,{\"statements\":[[0,\"                        \"],[1,[19,5,[\"cdna_end\"]],false],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                        -\\n\"]],\"parameters\":[]}],[0,\"                    \"],[8],[0,\"\\n                    \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,5,[\"consequence_terms\"]]],null,{\"statements\":[[0,\"                        \"],[4,\"each\",[[19,5,[\"consequence_terms\"]]],null,{\"statements\":[[1,[19,6,[]],false],[4,\"if\",[[25,\"neq\",[[19,6,[]],[19,5,[\"consequence_terms\",\"lastObject\"]]],null]],null,{\"statements\":[[0,\", \"]],\"parameters\":[]},null]],\"parameters\":[6]},null],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                        -\\n\"]],\"parameters\":[]}],[0,\"                    \"],[8],[0,\"\\n                    \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,5,[\"impact\"]],false],[8],[0,\"\\n                  \"],[8],[0,\"\\n\"]],\"parameters\":[]},null]],\"parameters\":[5]},null],[0,\"            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"]],\"parameters\":[]}]],\"parameters\":[]}],[4,\"if\",[[25,\"eq\",[[20,[\"variant\",\"variantstatus\",\"status\"]],\"MATCHED\"],null]],null,{\"statements\":[[0,\"          \"],[6,\"div\"],[9,\"class\",\"panel-heading panel-heading-middle\"],[7],[0,\"Matches\"],[8],[0,\"\\n          \"],[6,\"table\"],[9,\"class\",\"table table-condensed\"],[7],[0,\"\\n            \"],[6,\"thead\"],[7],[6,\"tr\"],[7],[6,\"th\"],[7],[0,\"#\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"DB\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Timestamp\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Match Type\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Phenotype Similarity\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Monarch Score \"],[6,\"nobr\"],[7],[0,\"(of 100)\"],[8],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Match Information\"],[8],[8],[8],[0,\"\\n            \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"sortedMatches\"]]],null,{\"statements\":[[0,\"                \"],[6,\"tr\"],[7],[0,\"\\n                  \"],[6,\"td\"],[7],[1,[25,\"plusone\",[[19,2,[]]],null],false],[8],[0,\"\\n                  \"],[6,\"td\"],[9,\"class\",\"logo-cell text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[25,\"eq\",[[19,1,[\"match\",\"database\"]],\"VarWatch\"],null]],null,{\"statements\":[[0,\"                      \"],[6,\"img\"],[9,\"class\",\"match-logo\"],[9,\"alt\",\"VarWatch\"],[9,\"src\",\"/assets/images/logo-no-text.png\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[25,\"eq\",[[19,1,[\"match\",\"database\"]],\"HGMD\"],null]],null,{\"statements\":[[0,\"                      \"],[6,\"img\"],[9,\"class\",\"match-logo\"],[9,\"alt\",\"HGMD\"],[9,\"src\",\"/assets/images/hgmd.png\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[25,\"eq\",[[19,1,[\"match\",\"database\"]],\"ClinVar\"],null]],null,{\"statements\":[[0,\"                      \"],[6,\"img\"],[9,\"class\",\"match-logo\"],[9,\"alt\",\"ClinVar\"],[9,\"src\",\"/assets/images/clinvar.png\"],[7],[8],[0,\"\\n                    \"]],\"parameters\":[]},null]],\"parameters\":[]}]],\"parameters\":[]}],[0,\"                  \"],[8],[0,\"\\n                  \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,1,[\"localedatetime\"]],false],[8],[0,\"\\n                  \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,1,[\"match\",\"matchType\"]]],null,{\"statements\":[[0,\"                      \"],[1,[25,\"match-type\",null,[[\"matchType\"],[[19,1,[\"match\",\"matchType\"]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"                  \"],[2,\"\\n{{#if vh.match.identicalMatch}}\\n                      <span class=\\\"glyphicon glyphicon-ok-sign green\\\" aria-hidden=\\\"true\\\"></span>\\n                    {{else}}\\n                      <span class=\\\"glyphicon glyphicon-remove-sign red\\\" aria-hidden=\\\"true\\\"></span>\\n                    {{/if}}                  \"],[0,\"\\n                  \"],[8],[0,\"\\n                  \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,1,[\"match\",\"phenotypeSimilarity\"]]],null,{\"statements\":[[0,\"                      \"],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-ok green\"],[9,\"aria-hidden\",\"true\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                      \"],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-remove red\"],[9,\"aria-hidden\",\"true\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]}],[0,\"                  \"],[8],[0,\"\\n                  \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,1,[\"match\",\"monarchScore\",\"noScore\"]]],null,{\"statements\":[[0,\"                      n/a\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[19,1,[\"match\",\"monarchScore\",\"isSettled\"]]],null,{\"statements\":[[0,\"                        \"],[1,[19,1,[\"match\",\"monarchScore\",\"mean\"]],false],[0,\"\\n                        \"],[2,\"\\n                        <nobr><span class=\\\"glyphicon glyphicon-arrow-right\\\"></span>{{vh.match.monarchScore.ltr}}</nobr> <nobr><span class=\\\"glyphicon glyphicon-arrow-left\\\"></span>{{vh.match.monarchScore.rtl}}</nobr>\\n                        \"],[0,\"\\n\"]],\"parameters\":[]},null]],\"parameters\":[]}],[0,\"                  \"],[8],[0,\"\\n                  \"],[6,\"td\"],[7],[0,\"\\n\"],[4,\"if\",[[19,1,[\"match\"]]],null,{\"statements\":[[0,\"                      \"],[6,\"dl\"],[9,\"class\",\"dl-horizontal no-bottom-margin\"],[7],[0,\"\\n\"],[4,\"if\",[[25,\"eq\",[[19,1,[\"match\",\"database\"]],\"HGMD\"],null]],null,{\"statements\":[[0,\"                        \"],[6,\"dt\"],[7],[0,\"Identifier\"],[8],[0,\"\\n                        \"],[6,\"dd\"],[7],[1,[19,1,[\"match\",\"accIdentifier\"]],false],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[25,\"eq\",[[19,1,[\"match\",\"database\"]],\"ClinVar\"],null]],null,{\"statements\":[[0,\"                        \"],[6,\"dt\"],[7],[8],[6,\"dd\"],[7],[0,\"n/a\"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[19,1,[\"match\",\"genes\"]]],null,{\"statements\":[[0,\"                          \"],[6,\"dt\"],[7],[0,\"Genes\"],[8],[0,\"\\n                          \"],[6,\"dd\"],[7],[4,\"each\",[[19,1,[\"match\",\"genes\"]]],null,{\"statements\":[[0,\"\\n                            \"],[6,\"a\"],[10,\"href\",[25,\"gen-gene-link\",[[19,4,[\"identifier\"]]],null],null],[9,\"target\",\"_blank\"],[7],[1,[19,4,[\"identifier\"]],false],[8],[4,\"if\",[[19,4,[\"ensembl\",\"description\"]]],null,{\"statements\":[[0,\" \"],[1,[25,\"ensembl-data\",null,[[\"ensembl\"],[[19,4,[\"ensembl\"]]]]],false]],\"parameters\":[]},null],[4,\"if\",[[25,\"neq\",[[19,4,[]],[19,1,[\"match\",\"genes\",\"lastObject\"]]],null]],null,{\"statements\":[[0,\", \"]],\"parameters\":[]},null],[0,\"\\n                          \"]],\"parameters\":[4]},null],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"if\",[[19,1,[\"match\",\"sortedHpoTerms\"]]],null,{\"statements\":[[0,\"                          \"],[6,\"dt\"],[7],[0,\"Phenotypes\"],[8],[0,\"\\n                          \"],[6,\"dd\"],[7],[4,\"each\",[[19,1,[\"match\",\"sortedHpoTerms\"]]],null,{\"statements\":[[0,\"\\n                            \"],[6,\"a\"],[10,\"href\",[25,\"gen-hpo-link\",[[19,3,[\"identifier\"]]],null],null],[9,\"target\",\"_blank\"],[7],[1,[19,3,[\"identifier\"]],false],[8],[0,\" \"],[1,[25,\"hpodescription\",[[19,3,[\"identifier\"]]],null],false],[4,\"if\",[[25,\"neq\",[[19,3,[]],[19,1,[\"match\",\"sortedHpoTerms\",\"lastObject\"]]],null]],null,{\"statements\":[[0,\", \"]],\"parameters\":[]},null],[0,\"\\n                          \"]],\"parameters\":[3]},null],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"if\",[[19,1,[\"match\",\"matchedVariant\"]]],null,{\"statements\":[[0,\"                          \"],[6,\"dt\"],[7],[0,\"Matched Variant\"],[8],[0,\"\\n                          \"],[6,\"dd\"],[7],[1,[19,1,[\"match\",\"matchedVariant\",\"chromosomeName\"]],false],[0,\":\"],[1,[19,1,[\"match\",\"matchedVariant\",\"position\"]],false],[0,\" \"],[1,[19,1,[\"match\",\"matchedVariant\",\"referenceBase\"]],false],[0,\"/\"],[1,[19,1,[\"match\",\"matchedVariant\",\"alternateBase\"]],false],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"if\",[[19,1,[\"match\",\"contact\"]]],null,{\"statements\":[[0,\"                          \"],[6,\"dt\"],[7],[0,\"Contact\"],[8],[0,\"\\n                          \"],[6,\"dd\"],[7],[6,\"a\"],[10,\"href\",[25,\"add\",[\"mailto:\",[19,1,[\"match\",\"contact\",\"href\"]]],null],null],[7],[1,[19,1,[\"match\",\"contact\",\"preName\"]],false],[0,\" \"],[1,[19,1,[\"match\",\"contact\",\"name\"]],false],[8],[0,\"\\n                          \"],[4,\"if\",[[19,1,[\"match\",\"contact\",\"phone\"]]],null,{\"statements\":[[0,\"☎\"],[1,[19,1,[\"match\",\"contact\",\"phone\"]],false]],\"parameters\":[]},null],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"                      \"]],\"parameters\":[]}]],\"parameters\":[]}],[0,\"                      \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"                  \"],[8],[0,\"\\n                \"],[8],[0,\"\\n\"]],\"parameters\":[1,2]},null],[0,\"            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"      \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/variant-line.hbs" } });
});
define("varwatch/templates/components/warning-message", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "z/lkKNhM", "block": "{\"symbols\":[],\"statements\":[[0,\"\\\"danger terror horror\\\"\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/components/warning-message.hbs" } });
});
define('varwatch/templates/components/x-select', ['exports', 'emberx-select/templates/components/x-select'], function (exports, _xSelect) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _xSelect.default;
    }
  });
});
define("varwatch/templates/convert", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "3c9n47jx", "block": "{\"symbols\":[\"data\",\"index\",\"data\",\"index\"],\"statements\":[[6,\"div\"],[9,\"class\",\"container main\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"flex1\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n        \"],[6,\"h1\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"HGVS Converter\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n        \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[3,\"action\",[[19,0,[]],\"convertData\"],[[\"on\"],[\"submit\"]]],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"referenceassembly\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Reference Assembly\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"label\"],[9,\"class\",\"radio-inline\"],[7],[0,\"\\n                \"],[1,[25,\"radio-button\",null,[[\"value\",\"groupValue\"],[\"GRCh38\",[20,[\"selectedReferenceAssembly\"]]]]],false],[0,\"GRCh38\\n              \"],[8],[0,\"\\n              \"],[6,\"label\"],[9,\"class\",\"radio-inline\"],[7],[0,\"\\n                \"],[1,[25,\"radio-button\",null,[[\"value\",\"groupValue\"],[\"GRCh37\",[20,[\"selectedReferenceAssembly\"]]]]],false],[0,\"GRCh37\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"hgvsterms\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"HGVS Terms\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"textarea\",null,[[\"class\",\"id\",\"spellcheck\",\"value\"],[\"form-control\",\"hgvsterms\",false,[20,[\"hgvsInput\"]]]]],false],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"submit\"],[9,\"class\",\"btn btn-default\"],[7],[0,\"Convert Terms\"],[8],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"resetForm\"]],[7],[0,\"Clear Input\"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n\"],[4,\"if\",[[20,[\"okData\"]]],null,{\"statements\":[[0,\"        \"],[6,\"div\"],[9,\"class\",\"panel-heading panel-heading-middle text-center\"],[7],[0,\"Successful Conversions\"],[8],[0,\"\\n        \"],[6,\"table\"],[9,\"class\",\"table table-condensed ensembl-table\"],[7],[0,\"\\n          \"],[6,\"thead\"],[7],[0,\"\\n            \"],[6,\"tr\"],[7],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"#\"],[8],[6,\"th\"],[7],[0,\"Input\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Chrom\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Pos\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Ref\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Alt\"],[8],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"okData\"]]],null,{\"statements\":[[0,\"              \"],[6,\"tr\"],[7],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[25,\"plusone\",[[19,4,[]]],null],false],[8],[0,\"\\n                \"],[6,\"td\"],[7],[1,[19,3,[\"input\"]],false],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,3,[\"chr\"]],false],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,3,[\"pos\"]],false],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,3,[\"ref\"]],false],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,3,[\"alt\"]],false],[8],[0,\"\\n              \"],[8],[0,\"\\n\"]],\"parameters\":[3,4]},null],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel-button-fullwidth\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"btn-group btn-group-justified neg-margin\"],[9,\"role\",\"group\"],[9,\"aria-label\",\"...\"],[7],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"btn-group\"],[9,\"role\",\"group\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default no-top-radius\"],[3,\"action\",[[19,0,[]],\"clearOkData\"]],[7],[0,\"Clear this List\"],[8],[0,\"\\n            \"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"btn-group\"],[9,\"role\",\"group\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default no-top-radius\"],[3,\"action\",[[19,0,[]],\"exportVcf\"]],[7],[0,\"Export as VCF\"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"      \"],[4,\"if\",[[25,\"and\",[[20,[\"failData\",\"length\"]],[20,[\"okData\",\"length\"]]],null]],null,{\"statements\":[[6,\"br\"],[7],[8]],\"parameters\":[]},null],[0,\"\\n\"],[4,\"if\",[[20,[\"failData\"]]],null,{\"statements\":[[0,\"        \"],[6,\"div\"],[9,\"class\",\"panel-heading panel-heading-middle text-center\"],[7],[0,\"Failed Conversions\"],[8],[0,\"\\n        \"],[6,\"table\"],[9,\"class\",\"table table-condensed ensembl-table\"],[7],[0,\"\\n          \"],[6,\"thead\"],[7],[0,\"\\n            \"],[6,\"tr\"],[7],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"#\"],[8],[6,\"th\"],[7],[0,\"Input\"],[8],[6,\"th\"],[7],[0,\"Error\"],[8],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"failData\"]]],null,{\"statements\":[[0,\"              \"],[6,\"tr\"],[7],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[25,\"plusone\",[[19,2,[]]],null],false],[8],[0,\"\\n                \"],[6,\"td\"],[7],[1,[19,1,[\"input\"]],false],[8],[0,\"\\n                \"],[6,\"td\"],[7],[1,[19,1,[\"errorString\"]],false],[8],[0,\"\\n              \"],[8],[0,\"\\n\"]],\"parameters\":[1,2]},null],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel-button-fullwidth\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"btn-group btn-group-justified neg-margin\"],[9,\"role\",\"group\"],[9,\"aria-label\",\"...\"],[7],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"btn-group\"],[9,\"role\",\"group\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default no-top-radius\"],[3,\"action\",[[19,0,[]],\"clearFailData\"]],[7],[0,\"Clear this List\"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[8]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/convert.hbs" } });
});
define("varwatch/templates/datasets", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "DPXQ2C4m", "block": "{\"symbols\":[],\"statements\":[[1,[18,\"liquid-outlet\"],false],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/datasets.hbs" } });
});
define("varwatch/templates/datasets/details", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "k8FGfqg7", "block": "{\"symbols\":[\"sh\",\"index\",\"ev\",\"index\",\"variant\",\"index\",\"annotation\"],\"statements\":[[6,\"div\"],[9,\"class\",\"container main\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"panel panel-default flex1\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n      \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Dataset Details\"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n      \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group no-bottom-margin\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"for\",\"preName\"],[9,\"class\",\"col-sm-3 control-label\"],[7],[0,\"Description\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-9\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"form-control-static break-word\"],[7],[1,[20,[\"model\",\"description\"]],false],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group small-bottom-margin\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"for\",\"name\"],[9,\"class\",\"col-sm-3 control-label\"],[7],[0,\"Phenotype\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-9\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"model\",\"sortedHpoTerms\"]]],null,{\"statements\":[[0,\"                \"],[6,\"a\"],[10,\"href\",[25,\"gen-hpo-link\",[[19,7,[\"identifier\"]]],null],null],[9,\"target\",\"_blank\"],[7],[1,[19,7,[\"identifier\"]],false],[8],[0,\"\\n                \"],[1,[25,\"hpodescription\",[[19,7,[\"identifier\"]]],null],false],[4,\"if\",[[25,\"neq\",[[19,7,[]],[20,[\"model\",\"sortedHpoTerms\",\"lastObject\"]]],null]],null,{\"statements\":[[0,\", \"]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[7]},null],[0,\"            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n\"],[4,\"if\",[[20,[\"model\",\"ageOfOnset\"]]],null,{\"statements\":[[0,\"          \"],[6,\"div\"],[9,\"class\",\"form-group small-bottom-margin\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"name\"],[9,\"class\",\"col-sm-3 control-label\"],[7],[0,\"Age at Onset\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-9\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"\\n                \"],[6,\"a\"],[10,\"href\",[25,\"gen-hpo-link\",[[20,[\"model\",\"ageOfOnset\"]]],null],null],[9,\"target\",\"_blank\"],[7],[1,[20,[\"model\",\"ageOfOnset\"]],false],[8],[0,\"\\n                \"],[1,[25,\"hpodescription\",[[20,[\"model\",\"ageOfOnset\"]]],null],false],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"if\",[[20,[\"model\",\"modeOfInheritance\"]]],null,{\"statements\":[[0,\"          \"],[6,\"div\"],[9,\"class\",\"form-group small-bottom-margin\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"name\"],[9,\"class\",\"col-sm-3 control-label\"],[7],[0,\"Mode of Inheritance\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-9\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"\\n                \"],[6,\"a\"],[10,\"href\",[25,\"gen-hpo-link\",[[20,[\"model\",\"modeOfInheritance\"]]],null],null],[9,\"target\",\"_blank\"],[7],[1,[20,[\"model\",\"modeOfInheritance\"]],false],[8],[0,\"\\n                \"],[1,[25,\"hpodescription\",[[20,[\"model\",\"modeOfInheritance\"]]],null],false],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"      \"],[8],[0,\"\\n\"],[4,\"if\",[[20,[\"model\",\"variants\"]]],null,{\"statements\":[[0,\"        \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"Variants\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"table-responsive\"],[7],[0,\"\\n            \"],[6,\"table\"],[9,\"class\",\"table table-condensed no-bottom-margin\"],[7],[0,\"\\n              \"],[6,\"thead\"],[7],[6,\"tr\"],[7],[6,\"th\"],[7],[0,\"#\"],[8],[6,\"th\"],[7],[0,\"Raw Input\"],[4,\"if\",[[20,[\"model\",\"assembly\"]]],null,{\"statements\":[[0,\" (\"],[1,[20,[\"model\",\"assembly\"]],false],[0,\")\"]],\"parameters\":[]},null],[8],[6,\"th\"],[7],[0,\"Chrom.\"],[8],[6,\"th\"],[7],[0,\"Pos.\"],[8],[6,\"th\"],[7],[0,\"Ref.\"],[8],[6,\"th\"],[7],[0,\"Alt.\"],[8],[6,\"th\"],[7],[0,\"Status\"],[8],[6,\"th\"],[7],[0,\"Timestamp\"],[8],[8],[8],[0,\"\\n\"],[4,\"each\",[[20,[\"model\",\"variants\"]]],null,{\"statements\":[[0,\"                 \"],[1,[25,\"variant-line\",null,[[\"variant\",\"index\",\"showDetails\",\"onShowDetailsToggled\"],[[19,5,[]],[19,6,[]],[25,\"array-contains\",[[20,[\"details\"]],[19,5,[\"id\"]]],null],[25,\"action\",[[19,0,[]],\"setParams\",[19,5,[\"id\"]]],null]]]],false],[0,\"\\n\"]],\"parameters\":[5,6]},null],[0,\"            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"if\",[[20,[\"model\",\"errorvariants\"]]],null,{\"statements\":[[0,\"        \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"panel-heading panel-heading-hover pointer\"],[3,\"action\",[[19,0,[]],\"toggleErrorVariants\"]],[7],[0,\"Rejected Variants (\"],[1,[20,[\"model\",\"errorvariants\",\"length\"]],false],[0,\" \"],[4,\"if\",[[25,\"eq\",[[20,[\"model\",\"errorvariants\",\"length\"]],1],null]],null,{\"statements\":[[0,\"entry\"]],\"parameters\":[]},{\"statements\":[[0,\"entries\"]],\"parameters\":[]}],[0,\")\"],[8],[0,\"\\n\"],[4,\"liquid-if\",[[20,[\"showErrorVariants\"]]],null,{\"statements\":[[0,\"            \"],[6,\"div\"],[9,\"class\",\"table-responsive\"],[7],[0,\"\\n              \"],[6,\"table\"],[9,\"class\",\"table table-condensed no-bottom-margin\"],[7],[0,\"\\n                \"],[6,\"thead\"],[7],[6,\"tr\"],[7],[6,\"th\"],[7],[0,\"#\"],[8],[6,\"th\"],[7],[0,\"Chrom.\"],[8],[6,\"th\"],[7],[0,\"Pos.\"],[8],[6,\"th\"],[7],[0,\"Ref.\"],[8],[6,\"th\"],[7],[0,\"Alt.\"],[8],[6,\"th\"],[7],[0,\"Reason of Rejection\"],[8],[6,\"th\"],[7],[0,\"Timestamp\"],[8],[8],[8],[0,\"\\n                \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"model\",\"errorvariants\"]]],null,{\"statements\":[[0,\"                    \"],[6,\"tr\"],[7],[0,\"\\n                      \"],[6,\"td\"],[7],[1,[25,\"plusone\",[[19,4,[]]],null],false],[8],[0,\"\\n\"],[4,\"if\",[[19,3,[\"chromosomeName\"]]],null,{\"statements\":[[0,\"                        \"],[6,\"td\"],[7],[1,[19,3,[\"chromosomeName\"]],false],[8],[0,\"\\n                        \"],[6,\"td\"],[7],[1,[19,3,[\"position\"]],false],[8],[0,\"\\n                        \"],[6,\"td\"],[7],[1,[19,3,[\"referenceBase\"]],false],[8],[0,\"\\n                        \"],[6,\"td\"],[7],[1,[19,3,[\"alternateBase\"]],false],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                        \"],[6,\"td\"],[9,\"colspan\",\"4\"],[7],[1,[19,3,[\"identifier\"]],false],[8],[0,\"\\n\"]],\"parameters\":[]}],[0,\"                      \"],[6,\"td\"],[9,\"class\",\"break-white-space\"],[7],[1,[19,3,[\"variantstatus\",\"description\"]],false],[8],[0,\"\\n                      \"],[6,\"td\"],[7],[1,[19,3,[\"variantstatus\",\"localedatetime\"]],false],[8],[0,\"\\n                    \"],[8],[0,\"\\n\"]],\"parameters\":[3,4]},null],[0,\"                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"        \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"      \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel-heading panel-heading-hover pointer\"],[3,\"action\",[[19,0,[]],\"toggleStatusHistory\"]],[7],[0,\"Status History\"],[8],[0,\"\\n\"],[4,\"liquid-if\",[[20,[\"showStatusHistory\"]]],null,{\"statements\":[[0,\"          \"],[6,\"div\"],[9,\"class\",\"table-responsive\"],[7],[0,\"\\n            \"],[6,\"table\"],[9,\"class\",\"table table-condensed no-bottom-margin\"],[7],[0,\"\\n              \"],[6,\"thead\"],[7],[6,\"tr\"],[7],[6,\"th\"],[7],[0,\"status\"],[8],[6,\"th\"],[7],[0,\"description\"],[8],[6,\"th\"],[7],[0,\"timestamp\"],[8],[8],[8],[0,\"\\n              \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"model\",\"statushistory\",\"statushistory\"]]],null,{\"statements\":[[0,\"                  \"],[6,\"tr\"],[7],[6,\"td\"],[7],[1,[19,1,[\"status\"]],false],[8],[6,\"td\"],[7],[1,[19,1,[\"description\"]],false],[8],[6,\"td\"],[7],[1,[19,1,[\"localedatetime\"]],false],[8],[8],[0,\"\\n\"]],\"parameters\":[1,2]},null],[0,\"              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[8]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/datasets/details.hbs" } });
});
define("varwatch/templates/datasets/index", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "XOLNOZ/T", "block": "{\"symbols\":[\"dataset\",\"index\"],\"statements\":[[6,\"div\"],[9,\"class\",\"container\"],[7],[0,\"\\n\"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n    \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Datasets (\"],[1,[20,[\"model\",\"length\"]],false],[0,\" \"],[4,\"if\",[[25,\"eq\",[[20,[\"model\",\"length\"]],1],null]],null,{\"statements\":[[0,\"entry\"]],\"parameters\":[]},{\"statements\":[[0,\"entries\"]],\"parameters\":[]}],[0,\")\"],[8],[0,\"\\n  \"],[8],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"table-responsive\"],[7],[0,\"\\n\"],[4,\"unless\",[[25,\"eq\",[[20,[\"model\",\"length\"]],0],null]],null,{\"statements\":[[0,\"      \"],[6,\"table\"],[9,\"class\",\"table table-condensed table-hover\"],[9,\"style\",\"width: 100%;\"],[7],[0,\"\\n        \"],[6,\"thead\"],[7],[0,\"\\n          \"],[6,\"tr\"],[7],[0,\"\\n            \"],[6,\"th\"],[7],[0,\"#\"],[8],[6,\"th\"],[7],[0,\"Description\"],[8],[6,\"th\"],[7],[0,\"Status\"],[8],[6,\"th\"],[7],[0,\"Comment\"],[8],[6,\"th\"],[7],[0,\"Timestamp\"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"sortedDatasets\"]]],null,{\"statements\":[[4,\"row-link-to\",[\"datasets.details\",[19,1,[\"id\"]]],[[\"class\"],[\"pointer\"]],{\"statements\":[[0,\"              \"],[6,\"td\"],[9,\"class\",\"break-white-space\"],[7],[1,[25,\"plusone\",[[19,2,[]]],null],false],[8],[0,\"\\n              \"],[1,[25,\"elide-cell\",null,[[\"class\",\"text\",\"maxWidth\"],[\"break-white-space\",[19,1,[\"description\"]],350]]],false],[0,\"\\n              \"],[6,\"td\"],[9,\"class\",\"break-white-space\"],[7],[1,[19,1,[\"status\",\"status\"]],false],[8],[0,\"\\n              \"],[6,\"td\"],[9,\"class\",\"break-white-space\"],[7],[1,[19,1,[\"status\",\"description\"]],false],[8],[0,\"\\n              \"],[6,\"td\"],[9,\"class\",\"break-white-space\"],[7],[1,[19,1,[\"status\",\"localedatetime\"]],false],[8],[0,\"\\n\"]],\"parameters\":[]},null]],\"parameters\":[1,2]},null],[0,\"        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"  \"],[8],[0,\"\\n\"],[8],[0,\"\\n\"],[8]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/datasets/index.hbs" } });
});
define("varwatch/templates/help", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "jfrNrGvA", "block": "{\"symbols\":[],\"statements\":[[1,[18,\"help-page\"],false]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/help.hbs" } });
});
define("varwatch/templates/impressum", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "UOuz+P+l", "block": "{\"symbols\":[],\"statements\":[[1,[18,\"outlet\"],false],[0,\"\\n\"],[6,\"div\"],[9,\"class\",\"container flex1\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n      \"],[6,\"h1\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Impressum\"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n      \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"class\",\"col-sm-5 control-label\"],[7],[0,\"Projektverantwortlich\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-7\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"Professor Michael Krawczak\"],[6,\"br\"],[7],[8],[0,\"Brunswiker Str. 10, 24105 Kiel\"],[6,\"br\"],[7],[8],[0,\"Tel.: +49 431 500-30700\"],[6,\"br\"],[7],[8],[0,\"Fax: +49 431 500-30704\"],[6,\"br\"],[7],[8],[0,\"E-Mail: \"],[6,\"a\"],[9,\"href\",\"mailto:krawczak@medinfo.uni-kiel.de\"],[7],[0,\"krawczak@medinfo.uni-kiel.de\"],[8],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Dr. Marc Höppner\"],[6,\"br\"],[7],[8],[0,\"E-Mail: \"],[6,\"a\"],[9,\"href\",\"mailto:m.hoeppner@ikmb.uni-kiel.de\"],[7],[0,\"m.hoeppner@ikmb.uni-kiel.de\"],[8],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"class\",\"col-sm-5 control-label\"],[7],[0,\"Inhalt und Technik\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-7\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"Broder Fredrich\"],[6,\"br\"],[7],[8],[0,\"E-Mail: \"],[6,\"a\"],[9,\"href\",\"mailto:b.fredrich@ikmb.uni-kiel.de\"],[7],[0,\"b.fredrich@ikmb.uni-kiel.de\"],[8],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"class\",\"col-sm-5 control-label\"],[7],[0,\"Wissenschaftliche Betreuung\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-7\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"Professor Thomas Wienker\"],[6,\"br\"],[7],[8],[0,\"E-Mail: \"],[6,\"a\"],[9,\"href\",\"mailto:wienker@molgen.mpg.de\"],[7],[0,\"wienker@molgen.mpg.de\"],[8],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[6,\"a\"],[9,\"href\",\"http://www.tmf-ev.de/Arbeitsgruppen_Foren/AGMolMed.aspx\"],[7],[0,\"TMF AG Molekulare Medizin\"],[8],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"class\",\"col-sm-5 control-label\"],[7],[0,\"Webseite\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-7\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"Marcus Schmöhl\"],[6,\"br\"],[7],[8],[0,\"E-Mail: \"],[6,\"a\"],[9,\"href\",\"mailto:m.schmoehl@ikmb.uni-kiel.de\"],[7],[0,\"m.schmoehl@ikmb.uni-kiel.de\"],[8],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"class\",\"col-sm-5 control-label\"],[7],[0,\"Technische Beratung\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-7\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[0,\"Olaf Junge\"],[6,\"br\"],[7],[8],[0,\"E-Mail: \"],[6,\"a\"],[9,\"href\",\"mailto:junge@medinfo.uni-kiel.de\"],[7],[0,\"junge@medinfo.uni-kiel.de\"],[8],[6,\"br\"],[7],[8],[6,\"br\"],[7],[8],[0,\"Sven Gundlach\"],[6,\"br\"],[7],[8],[0,\"E-Mail: \"],[6,\"a\"],[9,\"href\",\"mailto:gundlach@medinfo.uni-kiel.de\"],[7],[0,\"gundlach@medinfo.uni-kiel.de\"],[8],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"class\",\"col-sm-5 control-label\"],[7],[0,\"Gefördert durch\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-7\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[6,\"a\"],[9,\"href\",\"https://www.bmbf.de/\"],[7],[0,\"Bundesministerium für Bildung und Forschung\"],[8],[8],[0,\"\\n            \"],[6,\"img\"],[9,\"src\",\"/assets/images/bmbf-gef-logo.jpg\"],[9,\"width\",\"130\"],[7],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[8],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/impressum.hbs" } });
});
define("varwatch/templates/index", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "1B9fpWPe", "block": "{\"symbols\":[\"match\",\"index\"],\"statements\":[[6,\"div\"],[9,\"class\",\"container main\"],[7],[0,\"\\n\"],[1,[18,\"home-links\"],false],[0,\"\\n\"],[2,\"\\n<div class = \\\"container flex1\\\">\\n\"],[0,\"\\n\"],[4,\"if\",[[20,[\"currentlyLoading\"]]],null,{\"statements\":[[4,\"if\",[[20,[\"matchCount\"]]],null,{\"statements\":[[0,\"      \"],[6,\"h5\"],[9,\"class\",\"text-center vmargin-20px\"],[7],[0,\"Found \"],[1,[18,\"matchCount\"],false],[0,\" new matches. Gathering match data:  \"],[6,\"progress\"],[10,\"value\",[18,\"finishedPromises\"],null],[10,\"max\",[18,\"promiseCount\"],null],[7],[8],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"      \"],[6,\"h5\"],[9,\"class\",\"text-center vmargin-20px\"],[7],[0,\"Searching new Matches...\"],[8],[0,\"\\n\"]],\"parameters\":[]}]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[20,[\"model\",\"length\"]]],null,{\"statements\":[[0,\"      \"],[6,\"div\"],[9,\"class\",\"panel panel-primary vmargin-20px flex1\"],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel-heading panel-heading-middle text-center\"],[7],[0,\"New Matches (\"],[1,[20,[\"model\",\"length\"]],false],[0,\" \"],[4,\"if\",[[25,\"eq\",[[20,[\"model\",\"length\"]],1],null]],null,{\"statements\":[[0,\"entry\"]],\"parameters\":[]},{\"statements\":[[0,\"entries\"]],\"parameters\":[]}],[0,\")\"],[8],[0,\"\\n        \"],[6,\"table\"],[9,\"class\",\"table table-condensed\"],[7],[0,\"\\n          \"],[6,\"thead\"],[7],[6,\"tr\"],[7],[6,\"th\"],[7],[0,\"#\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"DB\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Dataset\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Variant\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Match Type\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Phenotype Similarity\"],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Monarch Score \"],[6,\"nobr\"],[7],[0,\"(of 100)\"],[8],[8],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Timestamp\"],[8],[8],[8],[0,\"\\n          \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"sortedMatches\"]]],null,{\"statements\":[[0,\"              \"],[6,\"tr\"],[7],[0,\"\\n                \"],[6,\"td\"],[7],[1,[25,\"plusone\",[[19,2,[]]],null],false],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"logo-cell text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[25,\"eq\",[[19,1,[\"database\"]],\"VarWatch\"],null]],null,{\"statements\":[[0,\"                    \"],[6,\"img\"],[9,\"class\",\"match-logo\"],[9,\"alt\",\"VarWatch\"],[9,\"src\",\"/assets/images/logo-no-text.png\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[25,\"eq\",[[19,1,[\"database\"]],\"HGMD\"],null]],null,{\"statements\":[[0,\"                    \"],[6,\"img\"],[9,\"class\",\"match-logo\"],[9,\"alt\",\"HGMD\"],[9,\"src\",\"/assets/images/hgmd.png\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[25,\"eq\",[[19,1,[\"database\"]],\"ClinVar\"],null]],null,{\"statements\":[[0,\"                    \"],[6,\"img\"],[9,\"class\",\"match-logo\"],[9,\"alt\",\"ClinVar\"],[9,\"src\",\"/assets/images/clinvar.png\"],[7],[8],[0,\"\\n                  \"]],\"parameters\":[]},null]],\"parameters\":[]}]],\"parameters\":[]}],[0,\"                \"],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n                  \"],[1,[19,1,[\"variantstatus\",\"variant\",\"dataset\",\"description\"]],false],[0,\"\\n                \"],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,1,[\"variantstatus\",\"variant\"]]],null,{\"statements\":[[4,\"link-to\",[\"datasets.details\",[19,1,[\"datasetId\"]],[25,\"query-params\",null,[[\"details\"],[[25,\"gen_array_for_param\",[[19,1,[\"queryVariantId\"]]],null]]]]],null,{\"statements\":[[0,\"                      \"],[1,[19,1,[\"variantstatus\",\"variant\",\"chromosomeName\"]],false],[0,\":\"],[1,[19,1,[\"variantstatus\",\"variant\",\"position\"]],false],[0,\" \"],[1,[19,1,[\"variantstatus\",\"variant\",\"referenceBase\"]],false],[0,\"/\"],[1,[19,1,[\"variantstatus\",\"variant\",\"alternateBase\"]],false],[0,\"\\n\"]],\"parameters\":[]},null]],\"parameters\":[]},null],[0,\"                \"],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,1,[\"matchType\"]]],null,{\"statements\":[[0,\"                    \"],[1,[25,\"match-type\",null,[[\"matchType\"],[[19,1,[\"matchType\"]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"                \"],[2,\"\\n{{#if match.identicalMatch}}\\n                    <span class=\\\"glyphicon glyphicon-ok-sign green\\\" aria-hidden=\\\"true\\\"></span>\\n                  {{else}}\\n                    <span class=\\\"glyphicon glyphicon-remove-sign red\\\" aria-hidden=\\\"true\\\"></span>\\n                  {{/if}}                \"],[0,\"\\n                \"],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,1,[\"phenotypeSimilarity\"]]],null,{\"statements\":[[0,\"                    \"],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-ok green\"],[9,\"aria-hidden\",\"true\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                    \"],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-remove red\"],[9,\"aria-hidden\",\"true\"],[7],[8],[0,\"\\n\"]],\"parameters\":[]}],[0,\"                \"],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n\"],[4,\"if\",[[19,1,[\"monarchScore\",\"noScore\"]]],null,{\"statements\":[[0,\"                    n/a\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[19,1,[\"monarchScore\",\"isSettled\"]]],null,{\"statements\":[[0,\"                      \"],[1,[19,1,[\"monarchScore\",\"mean\"]],false],[0,\"\\n                      \"],[2,\"\\n                        <nobr><span class=\\\"glyphicon glyphicon-arrow-right\\\"></span>{{match.monarchScore.ltr}}</nobr> <nobr><span class=\\\"glyphicon glyphicon-arrow-left\\\"></span>{{match.monarchScore.rtl}}</nobr>\\n                      \"],[0,\"\\n\"]],\"parameters\":[]},null]],\"parameters\":[]}],[0,\"                \"],[8],[0,\"\\n                \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[0,\"\\n                  \"],[1,[19,1,[\"variantstatus\",\"localedatetime\"]],false],[0,\"\\n                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n\"]],\"parameters\":[1,2]},null],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"panel-button-fullwidth\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"btn-group btn-group-justified neg-margin\"],[9,\"role\",\"group\"],[9,\"aria-label\",\"...\"],[7],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"btn-group\"],[9,\"role\",\"group\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-primary panel-button\"],[3,\"action\",[[19,0,[]],\"clearNewMatches\"]],[7],[0,\"Clear List\"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"      \"],[6,\"h5\"],[9,\"class\",\"text-center vmargin-20px\"],[7],[0,\"No new Matches\"],[8],[0,\"\\n\"]],\"parameters\":[]}]],\"parameters\":[]}],[8]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/index.hbs" } });
});
define("varwatch/templates/information", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "grVYCe0v", "block": "{\"symbols\":[],\"statements\":[[1,[18,\"outlet\"],false],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/information.hbs" } });
});
define("varwatch/templates/login", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "3Bh+oVv+", "block": "{\"symbols\":[],\"statements\":[[6,\"div\"],[9,\"class\",\"container main\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"flex1\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n        \"],[6,\"h1\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Sign in\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n        \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[3,\"action\",[[19,0,[]],\"authenticate\"],[[\"on\"],[\"submit\"]]],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"loginidentification\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"eMail\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"id\",\"class\",\"placeholder\",\"value\"],[\"loginidentification\",\"form-control\",\"Enter eMail\",[20,[\"identification\"]]]]],false],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"loginpassword\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Password\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"id\",\"type\",\"class\",\"placeholder\",\"value\"],[\"loginpassword\",\"password\",\"form-control\",\"Enter Password\",[20,[\"password\"]]]]],false],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"submit\"],[9,\"class\",\"btn btn-default\"],[7],[0,\"Sign in\"],[8],[0,\"\\n\"],[4,\"link-to\",[\"recover\"],[[\"class\"],[\"pull-right vmargin-7px\"]],{\"statements\":[[0,\"                Forgot your password?\\n\"]],\"parameters\":[]},null],[0,\"            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"well\"],[7],[0,\"\\n      \"],[6,\"p\"],[9,\"lang\",\"de\"],[9,\"class\",\"hyphens no-bottom-margin\"],[7],[0,\"VarWatch ist ein Werkzeug für akkreditierte Humangenetiker und Wissenschaftler, die im Rahmen Ihrer Tätigkeit mit genetischen Daten von Patienten arbeiten. Vermehrt werden durch die Hochdurchsatzsequenzierung genomische Varianten gefunden, bei denen ein Zusammenhang mit einem klinischen Phänotyp vermutet aber durch fehlende Beobachtungen in anderen Patienten nicht schlussendlich geklärt werden kann. VarWatch bietet hierfür nun eine unabhängige, nicht-kommerzielle Plattform an, auf der solche VUS (variants of unknown significance) registriert und mit externen Datenbanken und dem Variantenregister in VarWatch kontinuierlich abgeglichen werden können. Mögliche Übereinstimmungen zu anderen Fallbeschreibungen werden an die Variantenbesitzer übermittelt, um die Findung einer Diagnose für die betroffenen Patienten zu unterstützen.\"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[8],[0,\"\\n\"],[4,\"if\",[[20,[\"errorMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isError\"],[\"Sign in failed!\",[20,[\"errorMessage\"]],[25,\"action\",[[19,0,[]],\"doNotShowModal\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/login.hbs" } });
});
define("varwatch/templates/not-found", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "qr1PgXwy", "block": "{\"symbols\":[],\"statements\":[[6,\"h1\"],[7],[0,\"404 Not Found\"],[8],[0,\"\\n\"],[6,\"p\"],[7],[0,\"\\n  Perhaps you have a link that has changed.\\n\"],[8],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/not-found.hbs" } });
});
define("varwatch/templates/oauth", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "ztqezYwS", "block": "{\"symbols\":[],\"statements\":[[6,\"div\"],[9,\"class\",\"container main\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"flex1\"],[7],[0,\"\\n\"],[4,\"if\",[[20,[\"paramError\"]]],null,{\"statements\":[[0,\"      \"],[6,\"div\"],[9,\"class\",\"alert alert-danger\"],[9,\"role\",\"alert\"],[7],[1,[18,\"paramError\"],true],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[4,\"if\",[[20,[\"paramsOk\"]]],null,{\"statements\":[[0,\"    \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n        \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"The client application \"],[1,[18,\"clientName\"],false],[0,\" is requesting to access your VarWatch account\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n        \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[3,\"action\",[[19,0,[]],\"authenticate\"],[[\"on\"],[\"submit\"]]],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"oauthidentification\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"eMail\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"id\",\"disabled\",\"class\",\"placeholder\",\"value\"],[\"oauthidentification\",[20,[\"authenticationOk\"]],\"form-control\",\"Enter eMail\",[20,[\"identification\"]]]]],false],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"oauthpassword\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Password\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"id\",\"disabled\",\"type\",\"class\",\"placeholder\",\"value\"],[\"oauthpassword\",[20,[\"authenticationOk\"]],\"password\",\"form-control\",\"Enter Password\",[20,[\"password\"]]]]],false],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"],[4,\"liquid-if\",[[20,[\"authenticationOk\"]]],null,{\"statements\":[[0,\"            \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n              \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n                \"],[6,\"div\"],[9,\"class\",\"alert alert-success no-bottom-margin\"],[9,\"role\",\"alert\"],[7],[6,\"strong\"],[7],[0,\"Authentication Successful!\"],[8],[8],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n              \"],[6,\"label\"],[9,\"for\",\"oauthpassword\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Grant Access?\"],[8],[0,\"\\n              \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n                \"],[6,\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"allow\"]],[7],[0,\"Allow\"],[8],[0,\"\\n                \"],[6,\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"deny\"]],[7],[0,\"Deny\"],[8],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"            \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n              \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n                \"],[6,\"button\"],[9,\"type\",\"submit\"],[9,\"class\",\"btn btn-default\"],[7],[0,\"Authenticate\"],[8],[0,\"\\n                \"],[6,\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"deny\"]],[7],[0,\"Not Now\"],[8],[0,\"\\n\"],[4,\"link-to\",[\"recover\"],[[\"class\"],[\"pull-right vmargin-7px\"]],{\"statements\":[[0,\"                  Forgot your password?\\n\"]],\"parameters\":[]},null],[0,\"              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n              \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n                \"],[6,\"div\"],[7],[0,\"\\n\"],[4,\"link-to\",[\"registration\"],null,{\"statements\":[[0,\"                  No Account Yet? Click Here to Register!\\n\"]],\"parameters\":[]},null],[0,\"                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]}],[0,\"        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[2,\"\\n    <button {{action \\\"checkTarget\\\"}}> click me</button>\\n    \"],[0,\"\\n    \"]],\"parameters\":[]},null]],\"parameters\":[]}],[0,\"  \"],[8],[0,\"\\n\"],[8],[0,\"\\n\"],[4,\"if\",[[20,[\"errorMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isError\"],[\"Authentication failed!\",[20,[\"errorMessage\"]],[25,\"action\",[[19,0,[]],\"doNotShowModal\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/oauth.hbs" } });
});
define("varwatch/templates/recover", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "sR7gDQg9", "block": "{\"symbols\":[],\"statements\":[[6,\"div\"],[9,\"class\",\"container main\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"flex1\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n        \"],[6,\"h1\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Recover lost password\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n        \"],[6,\"ul\"],[9,\"class\",\"list-group\"],[7],[0,\"\\n          \"],[6,\"li\"],[9,\"class\",\"list-group-item bg-info\"],[7],[0,\"\\n            \"],[6,\"p\"],[9,\"class\",\"no-bottom-margin\"],[7],[0,\"If you have forgotten your password, you can request a new one by entering the email  address you provided upon registration and clicking the button below. A new password will then be sent to your email address and your current password will be invalidated.\"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"li\"],[9,\"class\",\"list-group-item\"],[7],[0,\"\\n            \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[3,\"action\",[[19,0,[]],\"requestPw\"],[[\"on\"],[\"submit\"]]],[7],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"emailNotOk\"]],[20,[\"showErrors\"]]],null]]],{\"statements\":[[0,\"                \"],[6,\"label\"],[9,\"for\",\"recoveridentification\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"eMail\"],[8],[0,\"\\n                \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n                  \"],[1,[25,\"input\",null,[[\"id\",\"class\",\"placeholder\",\"value\"],[\"recoveridentification\",\"form-control\",\"Enter eMail\",[20,[\"mail\"]]]]],false],[0,\"\\n                  \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"emailNotOk\"]],[20,[\"showErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"emailHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter a valid email address\"],[8]],\"parameters\":[]},null],[0,\"\\n                \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"              \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n                \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n                  \"],[6,\"button\"],[9,\"type\",\"submit\"],[9,\"class\",\"btn btn-default\"],[7],[0,\"Request new password\"],[8],[0,\"\\n                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n            \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[8],[0,\"\\n\\n\"],[4,\"if\",[[20,[\"successMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isSuccess\"],[\"Password sent!\",[20,[\"successMessage\"]],[25,\"action\",[[19,0,[]],\"closeModalDialogSuccess\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"\\n\"],[4,\"if\",[[20,[\"errorMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isError\"],[\"Error!\",[20,[\"errorMessage\"]],[25,\"action\",[[19,0,[]],\"closeModalDialogError\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/recover.hbs" } });
});
define("varwatch/templates/redirect", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "N1rV32FH", "block": "{\"symbols\":[\"key\",\"value\"],\"statements\":[[6,\"div\"],[9,\"class\",\"container main\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"flex1\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n        \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"URL Fragment Parameters\"],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n        \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[7],[0,\"\\n\"],[4,\"each\",[[25,\"-each-in\",[[20,[\"model\"]]],null]],null,{\"statements\":[[0,\"            \"],[6,\"div\"],[9,\"class\",\"form-group no-bottom-margin\"],[7],[0,\"\\n              \"],[6,\"label\"],[9,\"for\",\"preName\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[1,[19,1,[]],false],[8],[0,\"\\n              \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n                \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[19,2,[]],false],[8],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[1,2]},null],[0,\"        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[8]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/redirect.hbs" } });
});
define("varwatch/templates/registration", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "8c7K+IQb", "block": "{\"symbols\":[],\"statements\":[[6,\"div\"],[9,\"class\",\"container main\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"flex1\"],[7],[0,\"\\n    \"],[1,[18,\"registration-dialog\"],false],[0,\"\\n  \"],[8],[0,\"\\n\"],[8]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/registration.hbs" } });
});
define("varwatch/templates/submit", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "4ZYCOizT", "block": "{\"symbols\":[\"variant\",\"index\",\"xs\",\"chrom\",\"xs\",\"id\",\"xs\",\"od\"],\"statements\":[[6,\"div\"],[9,\"class\",\"container\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n      \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Submit Case Data\"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n      \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[3,\"action\",[[19,0,[]],\"submitData\"],[[\"on\"],[\"submit\"]]],[7],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"descriptionNotOk\"]]],null]]],{\"statements\":[[0,\"          \"],[6,\"label\"],[9,\"for\",\"description\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Case Identifier\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n            \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"description\",\"Enter Case Identifier\",[20,[\"description\"]]]]],false],[0,\"\\n            \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"descriptionNotOk\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"descriptionHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please provide a description.\"],[8]],\"parameters\":[]},null],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"for\",\"onset\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Patient Age of Onset\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n\"],[4,\"x-select\",null,[[\"value\",\"on-change\",\"class\",\"id\"],[[20,[\"onset\"]],[25,\"action\",[[19,0,[]],\"didMakeSelection\",\"onset\"],null],\"form-control\",\"onset\"]],{\"statements\":[[0,\"              \"],[4,\"component\",[[19,7,[\"option\"]]],[[\"value\"],[\"\"]],{\"statements\":[[0,\"Not Specified\"]],\"parameters\":[]},null],[0,\"\\n\"],[4,\"each\",[[20,[\"hpo\",\"onsetData\"]]],null,{\"statements\":[[0,\"                \"],[4,\"component\",[[19,7,[\"option\"]]],[[\"value\"],[[19,8,[\"term\"]]]],{\"statements\":[[1,[19,8,[\"term\"]],false],[0,\": \"],[1,[19,8,[\"desc\"]],false]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[8]},null]],\"parameters\":[7]},null],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"for\",\"inheritance\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Mode of Inheritance\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n\"],[4,\"x-select\",null,[[\"value\",\"on-change\",\"class\",\"id\"],[[20,[\"inheritance\"]],[25,\"action\",[[19,0,[]],\"didMakeSelection\",\"inheritance\"],null],\"form-control\",\"inheritance\"]],{\"statements\":[[0,\"              \"],[4,\"component\",[[19,5,[\"option\"]]],[[\"value\"],[\"\"]],{\"statements\":[[0,\"Not Specified\"]],\"parameters\":[]},null],[0,\"\\n\"],[4,\"each\",[[20,[\"hpo\",\"inheritanceData\"]]],null,{\"statements\":[[0,\"                \"],[4,\"component\",[[19,5,[\"option\"]]],[[\"value\"],[[19,6,[\"term\"]]]],{\"statements\":[[1,[19,6,[\"term\"]],false],[0,\": \"],[1,[19,6,[\"desc\"]],false]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[6]},null]],\"parameters\":[5]},null],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n\"],[4,\"form-group\",null,[[\"class\",\"hasError\"],[\"bottom-margin-5px\",[25,\"and\",[[20,[\"showErrors\"]],[20,[\"hpoTermsNotOk\"]]],null]]],{\"statements\":[[0,\"          \"],[6,\"label\"],[9,\"for\",\"hpoterms\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Phenotype (HPO)\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n            \"],[1,[25,\"textarea\",null,[[\"class\",\"id\",\"spellcheck\",\"value\"],[\"form-control\",\"hpoterms\",false,[20,[\"hpoterms\"]]]]],false],[0,\"\\n            \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"hpoTermsNotOk\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"descriptionHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please add at least one HPO term describing a phenotypic abnormality.\"],[8]],\"parameters\":[]},null],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n            \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"hpoPurgeExpand\"]],[7],[0,\"Purge and Expand\"],[8],[0,\"\\n            \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"hpoPurgeCollapse\"]],[7],[0,\"Purge and Collapse\"],[8],[0,\"\\n            \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"clearHpoTerms\"]],[7],[0,\"Clear\"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n            \"],[1,[25,\"hpo-browser\",null,[[\"addHpo\"],[[25,\"action\",[[19,0,[]],\"addHpo\"],null]]]],false],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"for\",\"variantinput\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Variant Input Format\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"class\",\"radio-inline\"],[7],[0,\"\\n              \"],[1,[25,\"radio-button\",null,[[\"value\",\"groupValue\"],[\"vcf\",[20,[\"selectedVariantInput\"]]]]],false],[0,\"VCF\\n            \"],[8],[0,\"\\n            \"],[6,\"label\"],[9,\"class\",\"radio-inline\"],[7],[0,\"\\n              \"],[1,[25,\"radio-button\",null,[[\"value\",\"groupValue\"],[\"hgvs\",[20,[\"selectedVariantInput\"]]]]],false],[0,\"HGVS\\n            \"],[8],[0,\"\\n            \"],[6,\"label\"],[9,\"class\",\"radio-inline\"],[7],[0,\"\\n              \"],[1,[25,\"radio-button\",null,[[\"value\",\"groupValue\"],[\"direct\",[20,[\"selectedVariantInput\"]]]]],false],[0,\"Direct\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"for\",\"referenceassembly\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Reference Assembly\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"class\",\"radio-inline\"],[7],[0,\"\\n              \"],[1,[25,\"radio-button\",null,[[\"value\",\"groupValue\"],[\"GRCh38\",[20,[\"selectedReferenceAssembly\"]]]]],false],[0,\"GRCh38\\n            \"],[8],[0,\"\\n            \"],[6,\"label\"],[9,\"class\",\"radio-inline\"],[7],[0,\"\\n              \"],[1,[25,\"radio-button\",null,[[\"value\",\"groupValue\"],[\"GRCh37\",[20,[\"selectedReferenceAssembly\"]]]]],false],[0,\"GRCh37\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n\"],[4,\"liquid-if\",[[25,\"eq\",[[20,[\"selectedVariantInput\"]],\"vcf\"],null]],[[\"class\"],[\"vcfsubmit\"]],{\"statements\":[[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"noFileSelected\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"vcffile\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"VCF File\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"div\"],[9,\"class\",\"alert alert-warning bottom-margin-5px\"],[7],[0,\"Please note that a VarWatch VCF must contain \"],[6,\"strong\"],[7],[0,\"only one patient\"],[8],[0,\"! Multi-sample VCFs will be treated as a single case submission.\"],[8],[0,\"\\n              \"],[1,[25,\"bootstrap-fileselect\",null,[[\"id\",\"file\",\"fileChanged\"],[\"vcffile\",[20,[\"vcffile\"]],\"updateVcfFile\"]]],false],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"noFileSelected\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"fileHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please select a file to upload.\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null]],\"parameters\":[]},null],[4,\"liquid-if\",[[25,\"eq\",[[20,[\"selectedVariantInput\"]],\"hgvs\"],null]],[[\"class\"],[\"hgvssubmit\"]],{\"statements\":[[0,\"          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"hgvsvariants\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Variant Input\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"div\"],[9,\"class\",\"input-group\"],[7],[0,\"\\n                \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"hgvsvariants\",\"Enter HGVS Term(s)\",[20,[\"hgvsinput\"]]]]],false],[0,\"\\n                \"],[6,\"span\"],[9,\"class\",\"input-group-btn\"],[7],[0,\"\\n                  \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"addHgvsVariants\"]],[7],[0,\"Add Variant(s)\"],[8],[0,\"\\n                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"hgvsVariantsEmpty\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"hgvsvariants\"],[9,\"class\",\"col-sm-2 control-label panel-label\"],[7],[0,\"Variants\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"hgvs-list\",null,[[\"hgvsVariants\",\"makeDanger\",\"removeVariant\"],[[20,[\"hgvsVariants\"]],[25,\"and\",[[20,[\"showErrors\"]],[20,[\"hgvsVariantsEmpty\"]]],null],[25,\"action\",[[19,0,[]],\"removeHgvsVariant\"],null]]]],false],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"hgvsVariantsEmpty\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"hgvsHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please add at least one variant.\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null]],\"parameters\":[]},null],[4,\"liquid-if\",[[25,\"eq\",[[20,[\"selectedVariantInput\"]],\"direct\"],null]],[[\"class\"],[\"directsubmit\"]],{\"statements\":[[0,\"          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"directvariants\"],[9,\"class\",\"col-sm-2 control-label panel-label\"],[7],[0,\"Variant Input\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"div\"],[9,\"class\",\"panel panel-default no-bottom-margin\"],[7],[0,\"\\n                \"],[6,\"div\"],[9,\"class\",\"panel-body form-panel-body\"],[7],[0,\"\\n                  \"],[6,\"div\"],[9,\"class\",\"col-sm-6\"],[7],[0,\"\\n                    \"],[6,\"div\"],[9,\"class\",\"small-margin-right\"],[7],[0,\"\\n                      \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n                        \"],[6,\"label\"],[9,\"for\",\"chromosome\"],[7],[0,\"Chromosome\"],[8],[0,\"\\n\"],[4,\"x-select\",null,[[\"value\",\"on-change\",\"class\",\"id\"],[[20,[\"chrom\"]],[25,\"action\",[[19,0,[]],\"didMakeSelection\",\"chrom\"],null],\"form-control\",\"chromosome\"]],{\"statements\":[[4,\"each\",[[20,[\"chromosomes\"]]],null,{\"statements\":[[0,\"                            \"],[4,\"component\",[[19,3,[\"option\"]]],[[\"value\"],[[19,4,[]]]],{\"statements\":[[1,[19,4,[]],false]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[4]},null]],\"parameters\":[3]},null],[0,\"                      \"],[8],[0,\"\\n                    \"],[8],[0,\"\\n                  \"],[8],[0,\"\\n                  \"],[6,\"div\"],[9,\"class\",\"col-sm-6\"],[7],[0,\"\\n                    \"],[6,\"div\"],[9,\"class\",\"small-margin-left\"],[7],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"showVariantErrors\"]],[20,[\"positionNotOk\"]]],null]]],{\"statements\":[[0,\"                        \"],[6,\"label\"],[9,\"for\",\"position\"],[9,\"class\",\"control-label\"],[7],[0,\"Position\"],[8],[0,\"\\n                        \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"position\",\"Enter Position\",[20,[\"position\"]]]]],false],[0,\"\\n                        \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showVariantErrors\"]],[20,[\"positionNotOk\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"positionHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter a valid position\"],[8]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"                    \"],[8],[0,\"\\n                  \"],[8],[0,\"\\n                  \"],[6,\"div\"],[9,\"class\",\"col-sm-6\"],[7],[0,\"\\n                    \"],[6,\"div\"],[9,\"class\",\"small-margin-right\"],[7],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"showVariantErrors\"]],[25,\"or\",[[20,[\"refNotOk\"]],[20,[\"refAndAltEmpty\"]]],null]],null]]],{\"statements\":[[0,\"                        \"],[6,\"label\"],[9,\"for\",\"referencebase\"],[9,\"class\",\"control-label\"],[7],[0,\"Reference Base(s)\"],[8],[0,\"\\n                        \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"referencebase\",\"Enter Reference Base(s)\",[20,[\"refbase\"]]]]],false],[0,\"\\n                        \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showVariantErrors\"]],[20,[\"refNotOk\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"referencebaseHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter valid base-values\"],[8]],\"parameters\":[]},null],[0,\"\\n                        \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showVariantErrors\"]],[20,[\"refAndAltEmpty\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"alternatebaseHelpBlock2\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Must not be empty if Alternate is also empty\"],[8]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"                    \"],[8],[0,\"\\n                  \"],[8],[0,\"\\n                  \"],[6,\"div\"],[9,\"class\",\"col-sm-6\"],[7],[0,\"\\n                    \"],[6,\"div\"],[9,\"class\",\"small-margin-left\"],[7],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"showVariantErrors\"]],[25,\"or\",[[20,[\"altNotOk\"]],[20,[\"refAndAltEmpty\"]]],null]],null]]],{\"statements\":[[0,\"                        \"],[6,\"label\"],[9,\"for\",\"altbase\"],[9,\"class\",\"control-label\"],[7],[0,\"Alternate Base(s)\"],[8],[0,\"\\n                        \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"altbase\",\"Enter Alternate Base(s)\",[20,[\"altbase\"]]]]],false],[0,\"\\n                        \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showVariantErrors\"]],[20,[\"altNotOk\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"alternatebaseHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter valid base-values\"],[8]],\"parameters\":[]},null],[0,\"\\n                        \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showVariantErrors\"]],[20,[\"refAndAltEmpty\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"alternatebaseHelpBlock2\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Must not be empty if Reference is also empty\"],[8]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"                    \"],[8],[0,\"\\n                  \"],[8],[0,\"\\n                \"],[8],[0,\"\\n                \"],[6,\"div\"],[9,\"class\",\"panel-button-fullwidth\"],[7],[0,\"\\n                  \"],[6,\"div\"],[9,\"class\",\"btn-group btn-group-justified neg-margin\"],[9,\"role\",\"group\"],[9,\"aria-label\",\"...\"],[7],[0,\"\\n                    \"],[6,\"div\"],[9,\"class\",\"btn-group\"],[9,\"role\",\"group\"],[7],[0,\"\\n                      \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default panel-button\"],[3,\"action\",[[19,0,[]],\"resetVariantInput\"]],[7],[0,\"Reset\"],[8],[0,\"\\n                    \"],[8],[0,\"\\n                    \"],[6,\"div\"],[9,\"class\",\"btn-group\"],[9,\"role\",\"group\"],[7],[0,\"\\n                      \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default panel-button\"],[3,\"action\",[[19,0,[]],\"addDirectVariant\"]],[7],[0,\"Add Variant\"],[8],[0,\"\\n                    \"],[8],[0,\"\\n                  \"],[8],[0,\"\\n                \"],[8],[0,\"\\n              \"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"directVariantsEmpty\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"directvariants\"],[9,\"class\",\"col-sm-2 control-label panel-label\"],[7],[0,\"Variants\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"div\"],[10,\"class\",[26,[\"panel panel-default no-bottom-margin \",[25,\"if\",[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"directVariantsEmpty\"]]],null],\"panel-danger\"],null]]]],[7],[0,\"\\n\"],[4,\"if\",[[20,[\"directVariants\"]]],null,{\"statements\":[[0,\"                  \"],[6,\"table\"],[9,\"class\",\"table table-condensed no-bottom-margin\"],[7],[0,\"\\n                    \"],[6,\"thead\"],[7],[0,\"\\n                      \"],[6,\"tr\"],[7],[0,\"\\n                        \"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"#\"],[8],[0,\"\\n                        \"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Chromosome\"],[8],[0,\"\\n                        \"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Position\"],[8],[0,\"\\n                        \"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Reference Base(s)\"],[8],[0,\"\\n                        \"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Alternate Base(s)\"],[8],[0,\"\\n                        \"],[2,\" <th class=\\\"text-center\\\">Reference Assembly</th> \"],[0,\"\\n                        \"],[6,\"th\"],[9,\"class\",\"text-center\"],[7],[0,\"Remove\"],[8],[0,\"\\n                      \"],[8],[0,\"\\n                    \"],[8],[0,\"\\n                    \"],[6,\"tbody\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"directVariants\"]]],null,{\"statements\":[[0,\"                        \"],[6,\"tr\"],[7],[0,\"\\n                          \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[25,\"plusone\",[[19,2,[]]],null],false],[8],[0,\"\\n                          \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,1,[\"variant\",\"referenceName\"]],false],[8],[0,\"\\n                          \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,1,[\"variant\",\"start\"]],false],[8],[0,\"\\n                          \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,1,[\"variant\",\"referenceBases\"]],false],[8],[0,\"\\n                          \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[1,[19,1,[\"variant\",\"alternateBases\"]],false],[8],[0,\"\\n                          \"],[2,\" <td class=\\\"text-center\\\">{{variant.variant.assembly}}</td> \"],[0,\"\\n                          \"],[6,\"td\"],[9,\"class\",\"text-center\"],[7],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default btn-xs\"],[9,\"aria-label\",\"Remove\"],[3,\"action\",[[19,0,[]],\"removeDirectVariant\",[19,2,[]]]],[7],[6,\"span\"],[9,\"class\",\"glyphicon glyphicon-remove\"],[9,\"aria-hidden\",\"true\"],[7],[8],[8],[8],[0,\"\\n                        \"],[8],[0,\"\\n\"]],\"parameters\":[1,2]},null],[0,\"                    \"],[8],[0,\"\\n                  \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"                  \"],[6,\"div\"],[9,\"class\",\"panel-body form-panel\"],[9,\"id\",\"directvariants\"],[7],[0,\"\\n                    no Variants\\n                  \"],[8],[0,\"\\n\"]],\"parameters\":[]}],[0,\"              \"],[8],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"showErrors\"]],[20,[\"directVariantsEmpty\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"directHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please add at least one variant.\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null]],\"parameters\":[]},null],[0,\"        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n            \"],[6,\"button\"],[9,\"type\",\"submit\"],[9,\"class\",\"btn btn-default\"],[7],[0,\"Submit\"],[8],[0,\"\\n            \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"resetForm\"]],[7],[0,\"Reset\"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[8],[0,\"\\n\\n\"],[4,\"if\",[[20,[\"errorMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isError\"],[\"Data submit failed!\",[20,[\"errorMessage\"]],[25,\"action\",[[19,0,[]],\"closeModalDialog\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"\\n\"],[4,\"if\",[[20,[\"successMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isSuccess\"],[\"Data submit successful!\",[20,[\"successMessage\"]],[25,\"action\",[[19,0,[]],\"closeModalDialog\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"\\n\"],[4,\"if\",[[20,[\"warningMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isWarning\"],[\"Concerning your HPO Terms!\",[20,[\"warningMessage\"]],[25,\"action\",[[19,0,[]],\"closeModalDialog\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/submit.hbs" } });
});
define("varwatch/templates/testdata", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "h8lkuZx+", "block": "{\"symbols\":[\"td\",\"index\",\"td\",\"index\"],\"statements\":[[6,\"div\"],[9,\"class\",\"container\"],[7],[0,\"\\n\"],[4,\"each\",[[20,[\"testData\"]]],null,{\"statements\":[[0,\"    \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n        \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Use Case \"],[1,[25,\"plusone\",[[19,4,[]]],null],false],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n        \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"class\",\"col-sm-5 control-label\"],[7],[0,\"VCF File\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-7\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[6,\"a\"],[9,\"target\",\"_blank\"],[10,\"href\",[25,\"concat\",[\"/assets/testdata/\",[19,3,[\"vcf\"]]],null],null],[7],[1,[19,3,[\"vcf\"]],false],[8],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"class\",\"col-sm-5 control-label\"],[7],[0,\"HPO Terms\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-7\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[6,\"a\"],[9,\"target\",\"_blank\"],[10,\"href\",[25,\"concat\",[\"/assets/testdata/\",[19,3,[\"hpo\"]]],null],null],[7],[1,[19,3,[\"hpo\"]],false],[8],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n\"]],\"parameters\":[3,4]},null],[4,\"each\",[[20,[\"testData2\"]]],null,{\"statements\":[[0,\"    \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n        \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Use Case \"],[1,[25,\"add\",[21,[19,2,[]]],null],false],[8],[0,\"\\n      \"],[8],[0,\"\\n      \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n        \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"class\",\"col-sm-5 control-label\"],[7],[0,\"Data\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-7\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[6,\"a\"],[9,\"target\",\"_blank\"],[10,\"href\",[25,\"concat\",[\"/assets/testdata/\",[19,1,[]]],null],null],[7],[1,[19,1,[]],false],[8],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n\"]],\"parameters\":[1,2]},null],[8],[0,\"\\n\"]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/testdata.hbs" } });
});
define("varwatch/templates/userinfo", ["exports"], function (exports) {
  "use strict";

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = Ember.HTMLBars.template({ "id": "K/DEDPZB", "block": "{\"symbols\":[\"xs\"],\"statements\":[[6,\"div\"],[9,\"class\",\"container\"],[7],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n      \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Personal Information\"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n      \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[7],[0,\"\\n\"],[4,\"if\",[[20,[\"editUserInfo\"]]],null,{\"statements\":[[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"firstNameNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"firstName\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Given Name\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\",\"aria-describedby\"],[\"form-control\",\"firstName\",\"Enter Given Name\",[20,[\"model\",\"firstName\"]],\"givenNameHelpBlock\"]]],false],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"firstNameNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"givenNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter your given name.\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"lastNameNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"lastName\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Family Name\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\",\"aria-describedby\"],[\"form-control\",\"lastName\",\"Enter Family Name\",[20,[\"model\",\"lastName\"]],\"familyNameHelpBlock\"]]],false],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"lastNameNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"familyNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter your family name.\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"institutionNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"institution\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Institution\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\",\"aria-describedby\"],[\"form-control\",\"institution\",\"Enter Institution\",[20,[\"model\",\"institution\"]],\"institutionHelpBlock\"]]],false],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"institutionNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter the name of your institution\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"phone\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Phone\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"phone\",\"Enter Phone\",[20,[\"model\",\"phone\"]]]]],false],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"href\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"eMail\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"mail\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"addressNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"address\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Address\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"address\",\"Enter Address\",[20,[\"model\",\"address\"]]]]],false],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"addressNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter your address\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"postalCodeNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"postalCode\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Postal Code\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"postalCode\",\"Enter Postal Code\",[20,[\"model\",\"postalCode\"]]]]],false],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"postalCodeNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter the postal code of your city\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"cityNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"city\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"City\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"city\",\"Enter City\",[20,[\"model\",\"city\"]]]]],false],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"cityNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter the name of your city\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"countryNotOk\"]],[20,[\"showFormErrors\"]]],null]]],{\"statements\":[[0,\"            \"],[6,\"label\"],[9,\"for\",\"country\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Country\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[1,[25,\"input\",null,[[\"class\",\"id\",\"placeholder\",\"value\"],[\"form-control\",\"country\",\"Enter Country\",[20,[\"model\",\"country\"]]]]],false],[0,\"\\n              \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"countryNotOk\"]],[20,[\"showFormErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"institutionNameHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter the name of your country\"],[8]],\"parameters\":[]},null],[0,\"\\n            \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"submit\"],[9,\"class\",\"btn btn-default\"],[10,\"disabled\",[25,\"not\",[[20,[\"model\",\"hasDirtyAttributes\"]]],null],null],[3,\"action\",[[19,0,[]],\"saveChanges\"]],[7],[0,\"Save Changes\"],[8],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"cancelEdit\"]],[7],[0,\"Cancel\"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},{\"statements\":[[0,\"          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"preName\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Given Name\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"firstName\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"name\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Family Name\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"lastName\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"institution\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Institution\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"institution\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"phone\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Phone\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"phone\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"href\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"eMail\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"mail\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"address\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Address\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"address\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"postcode\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Postal Code\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"postalCode\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"city\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"City\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"city\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"label\"],[9,\"for\",\"country\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Country\"],[8],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n              \"],[6,\"p\"],[9,\"class\",\"form-control-static\"],[7],[1,[20,[\"model\",\"country\"]],false],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n            \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n              \"],[6,\"button\"],[9,\"type\",\"button\"],[9,\"class\",\"btn btn-default\"],[3,\"action\",[[19,0,[]],\"editData\"]],[7],[0,\"Edit\"],[8],[0,\"\\n            \"],[8],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]}],[0,\"      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n   \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n      \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Reporting\"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n      \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[3,\"action\",[[19,0,[]],\"setReportingInterval\"],[[\"on\"],[\"submit\"]]],[7],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"label\"],[9,\"for\",\"reportingschedule\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Schedule\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n\"],[4,\"x-select\",null,[[\"value\",\"on-change\",\"class\",\"id\"],[[20,[\"shed\"]],[25,\"action\",[[19,0,[]],\"didMakeSelection\",\"ccc\"],null],\"form-control\",\"reportingschedule\"]],{\"statements\":[[0,\"              \"],[4,\"component\",[[19,1,[\"option\"]]],[[\"value\"],[\"never\"]],{\"statements\":[[0,\"never\"]],\"parameters\":[]},null],[0,\"\\n              \"],[4,\"component\",[[19,1,[\"option\"]]],[[\"value\"],[\"daily\"]],{\"statements\":[[0,\"daily\"]],\"parameters\":[]},null],[0,\"\\n              \"],[4,\"component\",[[19,1,[\"option\"]]],[[\"value\"],[\"weekly\"]],{\"statements\":[[0,\"weekly\"]],\"parameters\":[]},null],[0,\"\\n              \"],[4,\"component\",[[19,1,[\"option\"]]],[[\"value\"],[\"monthly\"]],{\"statements\":[[0,\"monthly\"]],\"parameters\":[]},null],[0,\"\\n\"]],\"parameters\":[1]},null],[0,\"          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n            \"],[6,\"button\"],[9,\"type\",\"submit\"],[9,\"class\",\"btn btn-default\"],[7],[0,\"Set Reporting Schedule\"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n  \"],[6,\"div\"],[9,\"class\",\"panel panel-default\"],[7],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-heading\"],[7],[0,\"\\n      \"],[6,\"h3\"],[9,\"class\",\"panel-title text-center\"],[7],[0,\"Change Password\"],[8],[0,\"\\n    \"],[8],[0,\"\\n    \"],[6,\"div\"],[9,\"class\",\"panel-body\"],[7],[0,\"\\n      \"],[6,\"form\"],[9,\"class\",\"form-horizontal\"],[3,\"action\",[[19,0,[]],\"changePassword\"],[[\"on\"],[\"submit\"]]],[7],[0,\"\\n\"],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"oldPasswordNotOk\"]],[20,[\"showErrors\"]]],null]]],{\"statements\":[[0,\"          \"],[6,\"label\"],[9,\"for\",\"oldpassword\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"Current Password\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n            \"],[1,[25,\"input\",null,[[\"type\",\"class\",\"id\",\"placeholder\",\"value\"],[\"password\",\"form-control\",\"oldpassword\",\"Enter current Password\",[20,[\"oldpassword\"]]]]],false],[0,\"\\n            \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"oldPasswordNotOk\"]],[20,[\"showErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"oldpasswordHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"Please enter your current password.\"],[8]],\"parameters\":[]},null],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"passwordNotOk\"]],[20,[\"showErrors\"]]],null]]],{\"statements\":[[0,\"          \"],[6,\"label\"],[9,\"for\",\"password\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"New Password\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n            \"],[1,[25,\"input\",null,[[\"type\",\"class\",\"id\",\"placeholder\",\"value\"],[\"password\",\"form-control\",\"password\",\"Enter new Password\",[20,[\"password\"]]]]],false],[0,\"\\n            \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"passwordNotOk\"]],[20,[\"showErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"passwordHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"New password must not be empty.\"],[8]],\"parameters\":[]},null],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[4,\"form-group\",null,[[\"hasError\"],[[25,\"and\",[[20,[\"passrepeatNotOk\"]],[20,[\"showErrors\"]]],null]]],{\"statements\":[[0,\"          \"],[6,\"label\"],[9,\"for\",\"repeatpassword\"],[9,\"class\",\"col-sm-2 control-label\"],[7],[0,\"New Password again\"],[8],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-10\"],[7],[0,\"\\n            \"],[1,[25,\"input\",null,[[\"type\",\"class\",\"id\",\"placeholder\",\"value\"],[\"password\",\"form-control\",\"repeatpassword\",\"Repeat new Password\",[20,[\"passrepeat\"]]]]],false],[0,\"\\n            \"],[4,\"liquid-if\",[[25,\"and\",[[20,[\"passrepeatNotOk\"]],[20,[\"showErrors\"]]],null]],null,{\"statements\":[[6,\"span\"],[9,\"id\",\"passwordrepeatHelpBlock\"],[9,\"class\",\"help-block no-bottom-margin\"],[7],[0,\"New passwords do not match.\"],[8]],\"parameters\":[]},null],[0,\"\\n          \"],[8],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"        \"],[6,\"div\"],[9,\"class\",\"form-group\"],[7],[0,\"\\n          \"],[6,\"div\"],[9,\"class\",\"col-sm-offset-2 col-sm-10\"],[7],[0,\"\\n            \"],[6,\"button\"],[9,\"type\",\"submit\"],[9,\"class\",\"btn btn-default\"],[7],[0,\"Set Password\"],[8],[0,\"\\n          \"],[8],[0,\"\\n        \"],[8],[0,\"\\n      \"],[8],[0,\"\\n    \"],[8],[0,\"\\n  \"],[8],[0,\"\\n\"],[8],[0,\"\\n\"],[4,\"if\",[[20,[\"successMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isSuccess\"],[[20,[\"successMessageTitle\"]],[20,[\"successMessage\"]],[25,\"action\",[[19,0,[]],\"closeModalDialogSuccess\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null],[0,\"\\n\"],[4,\"if\",[[20,[\"errorMessage\"]]],null,{\"statements\":[[0,\"  \"],[1,[25,\"to-elsewhere\",null,[[\"named\",\"send\"],[\"modal\",[25,\"hash\",null,[[\"title\",\"message\",\"closeClick\",\"isError\"],[[20,[\"errorMessageTitle\"]],[20,[\"errorMessage\"]],[25,\"action\",[[19,0,[]],\"closeModalDialogError\"],null],true]]]]]],false],[0,\"\\n\"]],\"parameters\":[]},null]],\"hasEval\":false}", "meta": { "moduleName": "varwatch/templates/userinfo.hbs" } });
});
define('varwatch/transitions', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  exports.default = function () {
    this.transition(this.fromRoute('login'), this.toRoute('registration'), this.use('toRight'), this.reverse('toLeft'));
    this.transition(this.fromRoute('index'), this.toRoute('submit'), this.use('toLeft'), this.reverse('toRight'));
    this.transition(this.fromRoute('index'), this.toRoute('datasets'), this.use('toLeft'), this.reverse('toRight'));

    /*this.transition(
      this.fromRoute('help'),
      this.toRoute('registration'),
      this.use('toRight'),
      this.reverse('toLeft')
    );
     this.transition(
      this.fromRoute('help'),
      this.toRoute('login'),
      this.use('toLeft'),
      this.reverse('toRight')
    );
     this.transition(
      this.fromRoute('recover'),
      this.toRoute('help'),
      this.use('toRight'),
      this.reverse('toLeft')
    );*/

    /*this.transition(
      this.fromRoute('login'),
      this.toRoute('index'),
      this.use('fade', { duration: 500 })
    );*/

    this.transition(this.fromRoute('login'), this.toRoute('recover'), this.use('toLeft'), this.reverse('toRight'));

    this.transition(this.fromRoute('registration'), this.toRoute('recover'), this.use('toLeft'), this.reverse('toRight'));

    /*this.transition(
     this.fromRoute('datasets.index'),
     this.toRoute('datasets.details'),
     this.use('explode', {
       matchBy: 'did',
       use: ['fly-to', {duration: 1000}],
     }, {
       use: ['scrollThen','toLeft', {duration: 500}]
     }),
     this.reverse('explode', {
       matchBy: 'did',
       use: ['fly-to', {duration: 1000}],
     }, {
       use: ['scrollThen','toRight', {duration: 500}]
     })
    );*/
    this.transition(this.fromRoute('datasets.index'), this.toRoute('datasets.details'), this.use('toLeft'), this.reverse('toRight'));
    this.transition(this.fromRoute('index'), this.toRoute('userinfo'), this.use('toLeft'), this.reverse('toRight'));
    this.transition(this.hasClass('hgvssubmit'), this.toValue(true), this.use('toLeft'), this.reverse('fade', { duration: 100 }));
    this.transition(this.hasClass('directsubmit'), this.toValue(true), this.use('toLeft'), this.reverse('fade', { duration: 100 }));
    this.transition(this.hasClass('vcfsubmit'), this.toValue(true), this.use('toLeft'), this.reverse('fade', { duration: 100 }));
  };
});
define('varwatch/transitions/cross-fade', ['exports', 'liquid-fire/transitions/cross-fade'], function (exports, _crossFade) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _crossFade.default;
    }
  });
});
define('varwatch/transitions/default', ['exports', 'liquid-fire/transitions/default'], function (exports, _default) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _default.default;
    }
  });
});
define('varwatch/transitions/explode', ['exports', 'liquid-fire/transitions/explode'], function (exports, _explode) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _explode.default;
    }
  });
});
define('varwatch/transitions/fade', ['exports', 'liquid-fire/transitions/fade'], function (exports, _fade) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _fade.default;
    }
  });
});
define('varwatch/transitions/flex-grow', ['exports', 'liquid-fire/transitions/flex-grow'], function (exports, _flexGrow) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _flexGrow.default;
    }
  });
});
define('varwatch/transitions/fly-to', ['exports', 'liquid-fire/transitions/fly-to'], function (exports, _flyTo) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _flyTo.default;
    }
  });
});
define('varwatch/transitions/move-over', ['exports', 'liquid-fire/transitions/move-over'], function (exports, _moveOver) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _moveOver.default;
    }
  });
});
define('varwatch/transitions/scale', ['exports', 'liquid-fire/transitions/scale'], function (exports, _scale) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _scale.default;
    }
  });
});
define('varwatch/transitions/scroll-then', ['exports', 'liquid-fire/transitions/scroll-then'], function (exports, _scrollThen) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _scrollThen.default;
    }
  });
});
define('varwatch/transitions/to-down', ['exports', 'liquid-fire/transitions/to-down'], function (exports, _toDown) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _toDown.default;
    }
  });
});
define('varwatch/transitions/to-left', ['exports', 'liquid-fire/transitions/to-left'], function (exports, _toLeft) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _toLeft.default;
    }
  });
});
define('varwatch/transitions/to-right', ['exports', 'liquid-fire/transitions/to-right'], function (exports, _toRight) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _toRight.default;
    }
  });
});
define('varwatch/transitions/to-up', ['exports', 'liquid-fire/transitions/to-up'], function (exports, _toUp) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _toUp.default;
    }
  });
});
define('varwatch/transitions/wait', ['exports', 'liquid-fire/transitions/wait'], function (exports, _wait) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  Object.defineProperty(exports, 'default', {
    enumerable: true,
    get: function () {
      return _wait.default;
    }
  });
});


define('varwatch/config/environment', [], function() {
  var prefix = 'varwatch';
try {
  var metaName = prefix + '/config/environment';
  var rawConfig = document.querySelector('meta[name="' + metaName + '"]').getAttribute('content');
  var config = JSON.parse(unescape(rawConfig));

  var exports = { 'default': config };

  Object.defineProperty(exports, '__esModule', { value: true });

  return exports;
}
catch(err) {
  throw new Error('Could not read config from meta tag with name "' + metaName + '".');
}

});

if (!runningTests) {
  require("varwatch/app")["default"].create({"name":"varwatch","version":"0.0.0+73a68e67"});
}
//# sourceMappingURL=varwatch.map
