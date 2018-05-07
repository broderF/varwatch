'use strict';

define('varwatch/tests/app.lint-test', [], function () {
  'use strict';

  QUnit.module('ESLint | app');

  QUnit.test('adapters/application.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'adapters/application.js should pass ESLint\n\n');
  });

  QUnit.test('adapters/dataset.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'adapters/dataset.js should pass ESLint\n\n5:32 - \'snapshot\' is defined but never used. (no-unused-vars)\n8:35 - \'snapshot\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('adapters/match.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'adapters/match.js should pass ESLint\n\n4:32 - \'snapshot\' is defined but never used. (no-unused-vars)\n7:35 - \'snapshot\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('adapters/status.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'adapters/status.js should pass ESLint\n\n4:35 - \'snapshot\' is defined but never used. (no-unused-vars)\n9:30 - \'DS\' is not defined. (no-undef)');
  });

  QUnit.test('adapters/userinfo.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'adapters/userinfo.js should pass ESLint\n\n8:28 - \'snapshot\' is defined but never used. (no-unused-vars)\n12:38 - \'snapshot\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('adapters/variant.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'adapters/variant.js should pass ESLint\n\n4:35 - \'snapshot\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('adapters/variantstatus.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'adapters/variantstatus.js should pass ESLint\n\n4:35 - \'snapshot\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('app.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'app.js should pass ESLint\n\n6:8 - \'TextField\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('authenticators/varwatchoauth2.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'authenticators/varwatchoauth2.js should pass ESLint\n\n');
  });

  QUnit.test('authorizers/varwatchoauth2-bearer.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'authorizers/varwatchoauth2-bearer.js should pass ESLint\n\n');
  });

  QUnit.test('components/bootstrap-fileselect.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'components/bootstrap-fileselect.js should pass ESLint\n\n12:7 - Use closure actions, unless you need bubbling (ember/closure-actions)');
  });

  QUnit.test('components/elide-cell.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/elide-cell.js should pass ESLint\n\n');
  });

  QUnit.test('components/ensembl-data.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/ensembl-data.js should pass ESLint\n\n');
  });

  QUnit.test('components/form-group.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/form-group.js should pass ESLint\n\n');
  });

  QUnit.test('components/help-page.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/help-page.js should pass ESLint\n\n');
  });

  QUnit.test('components/hgvs-list.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/hgvs-list.js should pass ESLint\n\n');
  });

  QUnit.test('components/home-links.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/home-links.js should pass ESLint\n\n');
  });

  QUnit.test('components/hpo-browser.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/hpo-browser.js should pass ESLint\n\n');
  });

  QUnit.test('components/match-type.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/match-type.js should pass ESLint\n\n');
  });

  QUnit.test('components/modal-target.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/modal-target.js should pass ESLint\n\n');
  });

  QUnit.test('components/registration-dialog.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'components/registration-dialog.js should pass ESLint\n\n36:35 - \'a3\' is defined but never used. (no-unused-vars)\n49:37 - \'a3\' is defined but never used. (no-unused-vars)\n54:17 - \'mailaddy\' is assigned a value but never used. (no-unused-vars)');
  });

  QUnit.test('components/row-link-to.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/row-link-to.js should pass ESLint\n\n');
  });

  QUnit.test('components/variant-line.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'components/variant-line.js should pass ESLint\n\n21:18 - Use brace expansion (ember/use-brace-expansion)');
  });

  QUnit.test('components/warning-message.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'components/warning-message.js should pass ESLint\n\n');
  });

  QUnit.test('controllers/application.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'controllers/application.js should pass ESLint\n\n');
  });

  QUnit.test('controllers/convert.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'controllers/convert.js should pass ESLint\n\n28:7 - \'saveAs\' is not defined. (no-undef)');
  });

  QUnit.test('controllers/datasets/details.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'controllers/datasets/details.js should pass ESLint\n\n');
  });

  QUnit.test('controllers/datasets/index.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'controllers/datasets/index.js should pass ESLint\n\n12:38 - \'duration\' is assigned a value but never used. (no-unused-vars)');
  });

  QUnit.test('controllers/help.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'controllers/help.js should pass ESLint\n\n');
  });

  QUnit.test('controllers/index.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'controllers/index.js should pass ESLint\n\n40:33 - \'a3\' is defined but never used. (no-unused-vars)\n46:35 - \'a3\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('controllers/login.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'controllers/login.js should pass ESLint\n\n');
  });

  QUnit.test('controllers/oauth.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'controllers/oauth.js should pass ESLint\n\n25:33 - \'a3\' is defined but never used. (no-unused-vars)\n37:35 - \'a3\' is defined but never used. (no-unused-vars)\n53:39 - \'a3\' is defined but never used. (no-unused-vars)\n60:41 - \'a3\' is defined but never used. (no-unused-vars)\n102:33 - \'a3\' is defined but never used. (no-unused-vars)\n132:35 - \'a3\' is defined but never used. (no-unused-vars)\n154:33 - \'a3\' is defined but never used. (no-unused-vars)\n165:35 - \'a3\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('controllers/recover.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'controllers/recover.js should pass ESLint\n\n28:35 - \'a3\' is defined but never used. (no-unused-vars)\n40:37 - \'a3\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('controllers/redirect.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'controllers/redirect.js should pass ESLint\n\n');
  });

  QUnit.test('controllers/submit.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'controllers/submit.js should pass ESLint\n\n113:13 - Unexpected console statement. (no-console)\n321:9 - Unexpected console statement. (no-console)\n322:9 - Unexpected console statement. (no-console)\n323:9 - Unexpected console statement. (no-console)\n336:33 - \'a3\' is defined but never used. (no-unused-vars)\n361:34 - \'data\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('controllers/testdata.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'controllers/testdata.js should pass ESLint\n\n');
  });

  QUnit.test('controllers/userinfo.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'controllers/userinfo.js should pass ESLint\n\n71:35 - \'a3\' is defined but never used. (no-unused-vars)\n88:37 - \'a3\' is defined but never used. (no-unused-vars)\n113:33 - \'a3\' is defined but never used. (no-unused-vars)\n121:35 - \'a3\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('helpers/add.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/add.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/and.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/and.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/array-contains.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/array-contains.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/eq.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/eq.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/gen-array-for-param.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/gen-array-for-param.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/gen-family-link.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/gen-family-link.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/gen-gene-link.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/gen-gene-link.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/gen-hpo-link.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/gen-hpo-link.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/gen-pathway-link.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/gen-pathway-link.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/gen-variant-status.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/gen-variant-status.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/get-hpo-desc.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/get-hpo-desc.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/get-kin-tooltip-text.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/get-kin-tooltip-text.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/getbarclass.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/getbarclass.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/getchildbarclass.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/getchildbarclass.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/getpredindex.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/getpredindex.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/hgvsnotgood.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/hgvsnotgood.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/hpodescription.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/hpodescription.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/neq.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/neq.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/not.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/not.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/or.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/or.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/plusone.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/plusone.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/strip-pathway.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/strip-pathway.js should pass ESLint\n\n');
  });

  QUnit.test('models/annotation.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/annotation.js should pass ESLint\n\n');
  });

  QUnit.test('models/custom-inflector-rules.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/custom-inflector-rules.js should pass ESLint\n\n');
  });

  QUnit.test('models/dataset.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/dataset.js should pass ESLint\n\n');
  });

  QUnit.test('models/errorvariant.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/errorvariant.js should pass ESLint\n\n');
  });

  QUnit.test('models/errorvariantstatus.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/errorvariantstatus.js should pass ESLint\n\n');
  });

  QUnit.test('models/family.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/family.js should pass ESLint\n\n');
  });

  QUnit.test('models/gene.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/gene.js should pass ESLint\n\n');
  });

  QUnit.test('models/match.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'models/match.js should pass ESLint\n\n53:25 - \'reason\' is defined but never used. (no-unused-vars)\n58:25 - \'reason\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('models/pathway.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/pathway.js should pass ESLint\n\n');
  });

  QUnit.test('models/status.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/status.js should pass ESLint\n\n');
  });

  QUnit.test('models/statushistory.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/statushistory.js should pass ESLint\n\n');
  });

  QUnit.test('models/userinfo.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/userinfo.js should pass ESLint\n\n');
  });

  QUnit.test('models/variant.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/variant.js should pass ESLint\n\n');
  });

  QUnit.test('models/variantstatus.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'models/variantstatus.js should pass ESLint\n\n');
  });

  QUnit.test('reopens/text-field.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'reopens/text-field.js should pass ESLint\n\n');
  });

  QUnit.test('resolver.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'resolver.js should pass ESLint\n\n');
  });

  QUnit.test('router.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'router.js should pass ESLint\n\n');
  });

  QUnit.test('routes/application.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/application.js should pass ESLint\n\n');
  });

  QUnit.test('routes/convert.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'routes/convert.js should pass ESLint\n\n4:31 - \'model\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('routes/datasets.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/datasets.js should pass ESLint\n\n');
  });

  QUnit.test('routes/datasets/details.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'routes/datasets/details.js should pass ESLint\n\n11:18 - \'transition\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('routes/datasets/index.js', function (assert) {
    assert.expect(1);
    assert.ok(false, 'routes/datasets/index.js should pass ESLint\n\n6:20 - \'transition\' is defined but never used. (no-unused-vars)\n14:31 - \'model\' is defined but never used. (no-unused-vars)');
  });

  QUnit.test('routes/help.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/help.js should pass ESLint\n\n');
  });

  QUnit.test('routes/impressum.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/impressum.js should pass ESLint\n\n');
  });

  QUnit.test('routes/index.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/index.js should pass ESLint\n\n');
  });

  QUnit.test('routes/login.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/login.js should pass ESLint\n\n');
  });

  QUnit.test('routes/not-found.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/not-found.js should pass ESLint\n\n');
  });

  QUnit.test('routes/oauth.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/oauth.js should pass ESLint\n\n');
  });

  QUnit.test('routes/recover.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/recover.js should pass ESLint\n\n');
  });

  QUnit.test('routes/redirect.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/redirect.js should pass ESLint\n\n');
  });

  QUnit.test('routes/registration.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/registration.js should pass ESLint\n\n');
  });

  QUnit.test('routes/submit.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/submit.js should pass ESLint\n\n');
  });

  QUnit.test('routes/testdata.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/testdata.js should pass ESLint\n\n');
  });

  QUnit.test('routes/userinfo.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'routes/userinfo.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/annotation.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/annotation.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/dataset.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/dataset.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/errorvariant.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/errorvariant.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/errorvariantstatus.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/errorvariantstatus.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/family.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/family.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/gene.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/gene.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/match.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/match.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/pathway.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/pathway.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/status.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/status.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/statushistory.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/statushistory.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/userinfo.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/userinfo.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/variant.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/variant.js should pass ESLint\n\n');
  });

  QUnit.test('serializers/variantstatus.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'serializers/variantstatus.js should pass ESLint\n\n');
  });

  QUnit.test('services/hpo.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'services/hpo.js should pass ESLint\n\n');
  });

  QUnit.test('transitions.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'transitions.js should pass ESLint\n\n');
  });
});
define('varwatch/tests/helpers/destroy-app', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = destroyApp;
  function destroyApp(application) {
    Ember.run(application, 'destroy');
  }
});
define('varwatch/tests/helpers/ember-simple-auth', ['exports', 'ember-simple-auth/authenticators/test'], function (exports, _test) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.authenticateSession = authenticateSession;
  exports.currentSession = currentSession;
  exports.invalidateSession = invalidateSession;


  var TEST_CONTAINER_KEY = 'authenticator:test'; /* global wait */

  function ensureAuthenticator(app, container) {
    var authenticator = container.lookup(TEST_CONTAINER_KEY);
    if (!authenticator) {
      app.register(TEST_CONTAINER_KEY, _test.default);
    }
  }

  function authenticateSession(app, sessionData) {
    var container = app.__container__;

    var session = container.lookup('service:session');
    ensureAuthenticator(app, container);
    session.authenticate(TEST_CONTAINER_KEY, sessionData);
    return wait();
  }

  function currentSession(app) {
    return app.__container__.lookup('service:session');
  }

  function invalidateSession(app) {
    var session = app.__container__.lookup('service:session');
    if (session.get('isAuthenticated')) {
      session.invalidate();
    }
    return wait();
  }
});
define('varwatch/tests/helpers/module-for-acceptance', ['exports', 'qunit', 'varwatch/tests/helpers/start-app', 'varwatch/tests/helpers/destroy-app'], function (exports, _qunit, _startApp, _destroyApp) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  exports.default = function (name) {
    var options = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

    (0, _qunit.module)(name, {
      beforeEach: function beforeEach() {
        this.application = (0, _startApp.default)();

        if (options.beforeEach) {
          return options.beforeEach.apply(this, arguments);
        }
      },
      afterEach: function afterEach() {
        var _this = this;

        var afterEach = options.afterEach && options.afterEach.apply(this, arguments);
        return Ember.RSVP.resolve(afterEach).then(function () {
          return (0, _destroyApp.default)(_this.application);
        });
      }
    });
  };
});
define('varwatch/tests/helpers/resolver', ['exports', 'varwatch/resolver', 'varwatch/config/environment'], function (exports, _resolver, _environment) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });


  var resolver = _resolver.default.create();

  resolver.namespace = {
    modulePrefix: _environment.default.modulePrefix,
    podModulePrefix: _environment.default.podModulePrefix
  };

  exports.default = resolver;
});
define('varwatch/tests/helpers/start-app', ['exports', 'varwatch/app', 'varwatch/config/environment'], function (exports, _app, _environment) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = startApp;
  function startApp(attrs) {
    var attributes = Ember.merge({}, _environment.default.APP);
    attributes.autoboot = true;
    attributes = Ember.merge(attributes, attrs); // use defaults, but you can override;

    return Ember.run(function () {
      var application = _app.default.create(attributes);
      application.setupForTesting();
      application.injectTestHelpers();
      return application;
    });
  }
});
define('varwatch/tests/helpers/x-select', ['exports'], function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.select = select;


  /**
   * Picks an option from the select and sets it to be `selected` in the DOM.
   *
   * @method select
   * @param {string|<jQuery>} selector - selector for the select to pick from.
   * @param {string} texts - text of the option you are picking
   */
  function select(selector) {
    for (var _len = arguments.length, texts = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
      texts[_key - 1] = arguments[_key];
    }

    var $select = selector instanceof Ember.$ ? selector : Ember.$(selector);
    var $options = $select.find('option');

    if (!$options.length) {
      throw 'No options found in ' + selector;
    }

    $options.each(function () {
      var _this = this;

      var $option = Ember.$(this);

      Ember.run(function () {
        _this.selected = texts.some(function (text) {
          // uppercase both texts so the helper isn't case sensastive.
          var optionText = $option.text().trim().toUpperCase();

          return optionText === text.toUpperCase();
        });

        if (_this.selected) {
          $option.prop('selected', true).trigger('change');
        }
      });
    });
  }
});
define('varwatch/tests/integration/components/application-topbar-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('application-topbar', 'Integration | Component | application topbar', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {
    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "zCdx0swZ",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"application-topbar\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "H4Rt5lWc",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"application-topbar\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/bootstrap-fileselect-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('bootstrap-fileselect', 'Integration | Component | bootstrap fileselect', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "LUo2fBM7",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"bootstrap-fileselect\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "69J2/0Of",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"bootstrap-fileselect\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/elide-cell-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('elide-cell', 'Integration | Component | elide cell', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "27A0Q9Ps",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"elide-cell\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "irt1PyNI",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"elide-cell\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/ensembl-data-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('ensembl-data', 'Integration | Component | ensembl data', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "sYZ49pFy",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"ensembl-data\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "nQxWaFJD",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"ensembl-data\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/form-group-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('form-group', 'Integration | Component | form group', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "J7vqr59A",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"form-group\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "XJzMH/UV",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"form-group\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/help-page-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('help-page', 'Integration | Component | help page', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "6HpKuCd+",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"help-page\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "pRTwdqZA",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"help-page\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/hgvs-list-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('hgvs-list', 'Integration | Component | hgvs list', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "StpUWmZc",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"hgvs-list\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "TlKWlVsl",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"hgvs-list\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/home-links-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('home-links', 'Integration | Component | home links', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "eeOlCACw",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"home-links\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "dZ8jiWic",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"home-links\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/hpo-browser-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('hpo-browser', 'Integration | Component | hpo browser', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {
    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "ubDucRcx",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"hpo-browser\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "AGYZV9uO",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"hpo-browser\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/match-type-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('match-type', 'Integration | Component | match type', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {
    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "M70EBMjS",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"match-type\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "0ILBh3dB",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"match-type\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/modal-target-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('modal-target', 'Integration | Component | modal target', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "EnK5hoa3",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"modal-target\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "f6M+ZvaE",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"modal-target\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/registration-dialog-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('registration-dialog', 'Integration | Component | registration dialog', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {
    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "8PqFwDFI",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"registration-dialog\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "18hwfdbZ",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"registration-dialog\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/row-link-to-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('row-link-to', 'Integration | Component | row link to', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {
    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "anB86mAy",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"row-link-to\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "BOQrKaka",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"row-link-to\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/variant-line-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('variant-line', 'Integration | Component | variant line', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {
    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "TZkVx5Sx",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"variant-line\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "elj/S/88",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"variant-line\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/components/warning-message-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('warning-message', 'Integration | Component | warning message', {
    integration: true
  });

  (0, _emberQunit.test)('it renders', function (assert) {

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template({
      "id": "cayMC+oS",
      "block": "{\"symbols\":[],\"statements\":[[1,[18,\"warning-message\"],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template({
      "id": "h29k1zFt",
      "block": "{\"symbols\":[],\"statements\":[[0,\"\\n\"],[4,\"warning-message\",null,null,{\"statements\":[[0,\"      template block text\\n\"]],\"parameters\":[]},null],[0,\"  \"]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), 'template block text');
  });
});
define('varwatch/tests/integration/helpers/gen-variant-status-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('gen-variant-status', 'helper:gen-variant-status', {
    integration: true
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it renders', function (assert) {
    this.set('inputValue', '1234');

    this.render(Ember.HTMLBars.template({
      "id": "vefgmWNd",
      "block": "{\"symbols\":[],\"statements\":[[1,[25,\"gen-variant-status\",[[20,[\"inputValue\"]]],null],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '1234');
  });
});
define('varwatch/tests/integration/helpers/not-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForComponent)('not', 'helper:not', {
    integration: true
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it renders', function (assert) {
    this.set('inputValue', '1234');

    this.render(Ember.HTMLBars.template({
      "id": "pJvHw/1C",
      "block": "{\"symbols\":[],\"statements\":[[1,[25,\"not\",[[20,[\"inputValue\"]]],null],false]],\"hasEval\":false}",
      "meta": {}
    }));

    assert.equal(this.$().text().trim(), '1234');
  });
});
define('varwatch/tests/test-helper', ['varwatch/app', 'varwatch/config/environment', '@ember/test-helpers', 'ember-qunit'], function (_app, _environment, _testHelpers, _emberQunit) {
  'use strict';

  (0, _testHelpers.setApplication)(_app.default.create(_environment.default.APP));

  (0, _emberQunit.start)();
});
define('varwatch/tests/tests.lint-test', [], function () {
  'use strict';

  QUnit.module('ESLint | tests');

  QUnit.test('helpers/destroy-app.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/destroy-app.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/module-for-acceptance.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/module-for-acceptance.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/resolver.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/resolver.js should pass ESLint\n\n');
  });

  QUnit.test('helpers/start-app.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'helpers/start-app.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/application-topbar-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/application-topbar-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/bootstrap-fileselect-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/bootstrap-fileselect-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/elide-cell-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/elide-cell-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/ensembl-data-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/ensembl-data-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/form-group-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/form-group-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/help-page-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/help-page-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/hgvs-list-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/hgvs-list-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/home-links-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/home-links-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/hpo-browser-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/hpo-browser-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/match-type-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/match-type-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/modal-target-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/modal-target-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/registration-dialog-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/registration-dialog-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/row-link-to-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/row-link-to-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/variant-line-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/variant-line-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/components/warning-message-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/components/warning-message-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/helpers/gen-variant-status-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/helpers/gen-variant-status-test.js should pass ESLint\n\n');
  });

  QUnit.test('integration/helpers/not-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'integration/helpers/not-test.js should pass ESLint\n\n');
  });

  QUnit.test('test-helper.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'test-helper.js should pass ESLint\n\n');
  });

  QUnit.test('unit/adapters/application-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/adapters/application-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/adapters/dataset-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/adapters/dataset-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/adapters/match-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/adapters/match-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/adapters/status-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/adapters/status-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/adapters/userinfo-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/adapters/userinfo-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/adapters/variant-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/adapters/variant-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/adapters/variantstatus-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/adapters/variantstatus-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/application-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/application-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/convert-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/convert-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/datasets/details-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/datasets/details-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/datasets/index-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/datasets/index-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/help-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/help-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/index-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/index-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/login-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/login-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/oauth-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/oauth-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/recover-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/recover-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/registration-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/registration-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/submit-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/submit-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/testdata-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/testdata-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/controllers/userinfo-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/controllers/userinfo-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/add-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/add-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/and-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/and-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/array-contains-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/array-contains-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/eq-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/eq-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/gen-array-for-helper-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/gen-array-for-helper-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/gen-family-link-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/gen-family-link-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/gen-gene-link-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/gen-gene-link-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/gen-hpo-link-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/gen-hpo-link-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/gen-pathway-link-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/gen-pathway-link-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/get-bar-class-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/get-bar-class-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/get-gene-name-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/get-gene-name-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/get-hpo-desc-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/get-hpo-desc-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/get-kin-tooltip-text-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/get-kin-tooltip-text-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/getbarclass-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/getbarclass-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/getchildbarclass-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/getchildbarclass-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/getpredindex-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/getpredindex-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/hgvs-not-good-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/hgvs-not-good-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/hgvsnotgood-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/hgvsnotgood-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/hpodescription-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/hpodescription-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/neq-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/neq-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/or-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/or-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/plusone-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/plusone-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/helpers/strip-pathway-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/helpers/strip-pathway-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/initializers/hpo-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/initializers/hpo-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/annotation-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/annotation-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/dataset-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/dataset-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/errorvariants-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/errorvariants-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/errorvariantstatus-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/errorvariantstatus-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/family-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/family-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/gene-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/gene-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/information/dataset-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/information/dataset-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/information/datasets/:id/variants-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/information/datasets/:id/variants-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/match-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/match-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/pathway-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/pathway-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/status-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/status-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/statushistory-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/statushistory-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/userinfo-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/userinfo-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/variant-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/variant-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/variantstatus-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/variantstatus-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/models/variantstatushistory-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/models/variantstatushistory-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/application-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/application-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/convert-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/convert-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/datasets/details-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/datasets/details-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/datasets/index-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/datasets/index-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/help-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/help-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/impressum-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/impressum-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/index-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/index-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/information-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/information-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/information/datasets-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/information/datasets-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/login-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/login-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/not-found-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/not-found-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/oauth-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/oauth-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/recover-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/recover-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/redirect-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/redirect-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/registration-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/registration-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/sdatasets-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/sdatasets-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/submit-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/submit-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/testdata-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/testdata-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/routes/userinfo-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/routes/userinfo-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/annotation-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/annotation-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/dataset-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/dataset-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/errorvariants-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/errorvariants-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/errorvariantstatus-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/errorvariantstatus-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/family-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/family-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/gene-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/gene-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/match-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/match-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/pathway-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/pathway-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/status-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/status-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/statushistory-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/statushistory-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/userinfo-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/userinfo-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/variant-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/variant-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/variantstatus-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/variantstatus-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/serializers/variantstatushistory-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/serializers/variantstatushistory-test.js should pass ESLint\n\n');
  });

  QUnit.test('unit/services/hpoinfo-test.js', function (assert) {
    assert.expect(1);
    assert.ok(true, 'unit/services/hpoinfo-test.js should pass ESLint\n\n');
  });
});
define('varwatch/tests/unit/adapters/application-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('adapter:application', 'Unit | Adapter | application', {
    // Specify the other units that are required for this test.
    // needs: ['serializer:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });
});
define('varwatch/tests/unit/adapters/dataset-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('adapter:dataset', 'Unit | Adapter | dataset', {
    // Specify the other units that are required for this test.
    // needs: ['serializer:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });
});
define('varwatch/tests/unit/adapters/match-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('adapter:match', 'Unit | Adapter | match', {
    // Specify the other units that are required for this test.
    // needs: ['serializer:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });
});
define('varwatch/tests/unit/adapters/status-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('adapter:status', 'Unit | Adapter | status', {
    // Specify the other units that are required for this test.
    // needs: ['serializer:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });
});
define('varwatch/tests/unit/adapters/userinfo-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('adapter:userinfo', 'Unit | Adapter | userinfo', {
    // Specify the other units that are required for this test.
    // needs: ['serializer:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });
});
define('varwatch/tests/unit/adapters/variant-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('adapter:variant', 'Unit | Adapter | variant', {
    // Specify the other units that are required for this test.
    // needs: ['serializer:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });
});
define('varwatch/tests/unit/adapters/variantstatus-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('adapter:variantstatus', 'Unit | Adapter | variantstatus', {
    // Specify the other units that are required for this test.
    // needs: ['serializer:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });
});
define('varwatch/tests/unit/controllers/application-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:application', 'Unit | Controller | application', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/convert-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:convert', 'Unit | Controller | convert', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/datasets/details-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:datasets/details', 'Unit | Controller | datasets/details', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/datasets/index-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:datasets/index', 'Unit | Controller | datasets/index', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/help-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:help', 'Unit | Controller | help', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/index-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:index', 'Unit | Controller | index', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/login-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:login', 'Unit | Controller | login', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/oauth-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:oauth', 'Unit | Controller | oauth', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/recover-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:recover', 'Unit | Controller | recover', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/registration-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:registration', 'Unit | Controller | registration', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/submit-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:submit', 'Unit | Controller | submit', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/testdata-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:testdata', 'Unit | Controller | testdata', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/controllers/userinfo-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('controller:userinfo', 'Unit | Controller | userinfo', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });
});
define('varwatch/tests/unit/helpers/add-test', ['varwatch/helpers/add', 'qunit'], function (_add, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | add');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _add.add)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/and-test', ['varwatch/helpers/and', 'qunit'], function (_and, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | and');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _and.and)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/array-contains-test', ['varwatch/helpers/array-contains', 'qunit'], function (_arrayContains, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | array contains');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _arrayContains.arrayContains)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/eq-test', ['varwatch/helpers/eq', 'qunit'], function (_eq, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | eq');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _eq.eq)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/gen-array-for-helper-test', ['varwatch/helpers/gen-array-for-helper', 'qunit'], function (_genArrayForHelper, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | gen array for helper');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _genArrayForHelper.genArrayForHelper)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/gen-family-link-test', ['varwatch/helpers/gen-family-link', 'qunit'], function (_genFamilyLink, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | gen family link');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _genFamilyLink.genFamilyLink)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/gen-gene-link-test', ['varwatch/helpers/gen-gene-link', 'qunit'], function (_genGeneLink, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | gen gene link');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _genGeneLink.genGeneLink)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/gen-hpo-link-test', ['varwatch/helpers/gen-hpo-link', 'qunit'], function (_genHpoLink, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | gen hpo link');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _genHpoLink.genHpoLink)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/gen-pathway-link-test', ['varwatch/helpers/gen-pathway-link', 'qunit'], function (_genPathwayLink, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | gen pathway link');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _genPathwayLink.genPathwayLink)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/get-bar-class-test', ['varwatch/helpers/get-bar-class', 'qunit'], function (_getBarClass, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | get bar class');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _getBarClass.getBarClass)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/get-gene-name-test', ['varwatch/helpers/get-gene-name', 'qunit'], function (_getGeneName, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | get gene name');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _getGeneName.getGeneName)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/get-hpo-desc-test', ['varwatch/helpers/get-hpo-desc', 'qunit'], function (_getHpoDesc, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | get hpo desc');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _getHpoDesc.getHpoDesc)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/get-kin-tooltip-text-test', ['varwatch/helpers/get-kin-tooltip-text', 'qunit'], function (_getKinTooltipText, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | get kin tooltip text');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _getKinTooltipText.getKinTooltipText)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/getbarclass-test', ['varwatch/helpers/getbarclass', 'qunit'], function (_getbarclass, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | getbarclass');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _getbarclass.getbarclass)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/getchildbarclass-test', ['varwatch/helpers/getchildbarclass', 'qunit'], function (_getchildbarclass, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | getchildbarclass');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _getchildbarclass.getchildbarclass)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/getpredindex-test', ['varwatch/helpers/getpredindex', 'qunit'], function (_getpredindex, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | getpredindex');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _getpredindex.getpredindex)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/hgvs-not-good-test', ['varwatch/helpers/hgvs-not-good', 'qunit'], function (_hgvsNotGood, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | hgvs not good');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _hgvsNotGood.hgvsNotGood)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/hgvsnotgood-test', ['varwatch/helpers/hgvsnotgood', 'qunit'], function (_hgvsnotgood, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | hgvsnotgood');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _hgvsnotgood.hgvsnotgood)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/hpodescription-test', ['varwatch/helpers/hpodescription', 'qunit'], function (_hpodescription, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | hpodescription');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _hpodescription.hpodescription)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/neq-test', ['varwatch/helpers/neq', 'qunit'], function (_neq, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | neq');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _neq.neq)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/or-test', ['varwatch/helpers/or', 'qunit'], function (_or, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | or');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _or.or)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/plusone-test', ['varwatch/helpers/plusone', 'qunit'], function (_plusone, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | plusone');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _plusone.plusone)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/helpers/strip-pathway-test', ['varwatch/helpers/strip-pathway', 'qunit'], function (_stripPathway, _qunit) {
  'use strict';

  (0, _qunit.module)('Unit | Helper | strip pathway');

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    var result = (0, _stripPathway.stripPathway)([42]);
    assert.ok(result);
  });
});
define('varwatch/tests/unit/initializers/hpo-test', ['varwatch/initializers/hpo', 'qunit', 'varwatch/tests/helpers/destroy-app'], function (_hpo, _qunit, _destroyApp) {
  'use strict';

  (0, _qunit.module)('Unit | Initializer | hpo', {
    beforeEach: function beforeEach() {
      var _this = this;

      Ember.run(function () {
        _this.application = Ember.Application.create();
        _this.application.deferReadiness();
      });
    },
    afterEach: function afterEach() {
      (0, _destroyApp.default)(this.application);
    }
  });

  // Replace this with your real tests.
  (0, _qunit.test)('it works', function (assert) {
    (0, _hpo.initialize)(this.application);

    // you would normally confirm the results of the initializer here
    assert.ok(true);
  });
});
define('varwatch/tests/unit/models/annotation-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('annotation', 'Unit | Model | annotation', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/dataset-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('dataset', 'Unit | Model | dataset', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/errorvariants-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('errorvariants', 'Unit | Model | errorvariants', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/errorvariantstatus-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('errorvariantstatus', 'Unit | Model | errorvariantstatus', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/family-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('family', 'Unit | Model | family', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/gene-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('gene', 'Unit | Model | gene', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/information/dataset-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('information/dataset', 'Unit | Model | information/dataset', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/information/datasets/:id/variants-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('information/datasets/:id/variants', 'Unit | Model | information/datasets/:id/variants', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/match-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('match', 'Unit | Model | match', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/pathway-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('pathway', 'Unit | Model | pathway', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/status-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('status', 'Unit | Model | status', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/statushistory-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('statushistory', 'Unit | Model | statushistory', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/userinfo-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('userinfo', 'Unit | Model | userinfo', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/variant-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('variant', 'Unit | Model | variant', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/variantstatus-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('variantstatus', 'Unit | Model | variantstatus', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/models/variantstatushistory-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('variantstatushistory', 'Unit | Model | variantstatushistory', {
    // Specify the other units that are required for this test.
    needs: []
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var model = this.subject();
    // let store = this.store();
    assert.ok(!!model);
  });
});
define('varwatch/tests/unit/routes/application-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:application', 'Unit | Route | application', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/convert-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:convert', 'Unit | Route | convert', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/datasets/details-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:datasets/details', 'Unit | Route | datasets/details', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/datasets/index-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:datasets/index', 'Unit | Route | datasets/index', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/help-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:help', 'Unit | Route | help', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/impressum-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:impressum', 'Unit | Route | impressum', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/index-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:index', 'Unit | Route | index', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/information-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:information', 'Unit | Route | information', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/information/datasets-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:information/datasets', 'Unit | Route | information/datasets', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/login-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:login', 'Unit | Route | login', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/not-found-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:not-found', 'Unit | Route | not found', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/oauth-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:oauth', 'Unit | Route | oauth', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/recover-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:recover', 'Unit | Route | recover', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/redirect-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:redirect', 'Unit | Route | redirect', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/registration-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:registration', 'Unit | Route | registration', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/sdatasets-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:sdatasets', 'Unit | Route | sdatasets', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/submit-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:submit', 'Unit | Route | submit', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/testdata-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:testdata', 'Unit | Route | testdata', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/routes/userinfo-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('route:userinfo', 'Unit | Route | userinfo', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  (0, _emberQunit.test)('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });
});
define('varwatch/tests/unit/serializers/annotation-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('annotation', 'Unit | Serializer | annotation', {
    // Specify the other units that are required for this test.
    needs: ['serializer:annotation']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/dataset-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('dataset', 'Unit | Serializer | dataset', {
    // Specify the other units that are required for this test.
    needs: ['serializer:dataset']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/errorvariants-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('errorvariants', 'Unit | Serializer | errorvariants', {
    // Specify the other units that are required for this test.
    needs: ['serializer:errorvariants']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/errorvariantstatus-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('errorvariantstatus', 'Unit | Serializer | errorvariantstatus', {
    // Specify the other units that are required for this test.
    needs: ['serializer:errorvariantstatus']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/family-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('family', 'Unit | Serializer | family', {
    // Specify the other units that are required for this test.
    needs: ['serializer:family']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/gene-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('gene', 'Unit | Serializer | gene', {
    // Specify the other units that are required for this test.
    needs: ['serializer:gene']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/match-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('match', 'Unit | Serializer | match', {
    // Specify the other units that are required for this test.
    needs: ['serializer:match']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/pathway-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('pathway', 'Unit | Serializer | pathway', {
    // Specify the other units that are required for this test.
    needs: ['serializer:pathway']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/status-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('status', 'Unit | Serializer | status', {
    // Specify the other units that are required for this test.
    needs: ['serializer:status']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/statushistory-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('statushistory', 'Unit | Serializer | statushistory', {
    // Specify the other units that are required for this test.
    needs: ['serializer:statushistory']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/userinfo-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('userinfo', 'Unit | Serializer | userinfo', {
    // Specify the other units that are required for this test.
    needs: ['serializer:userinfo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/variant-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('variant', 'Unit | Serializer | variant', {
    // Specify the other units that are required for this test.
    needs: ['serializer:variant']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/variantstatus-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('variantstatus', 'Unit | Serializer | variantstatus', {
    // Specify the other units that are required for this test.
    needs: ['serializer:variantstatus']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/serializers/variantstatushistory-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleForModel)('variantstatushistory', 'Unit | Serializer | variantstatushistory', {
    // Specify the other units that are required for this test.
    needs: ['serializer:variantstatushistory']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });
});
define('varwatch/tests/unit/services/hpoinfo-test', ['ember-qunit'], function (_emberQunit) {
  'use strict';

  (0, _emberQunit.moduleFor)('service:hpoinfo', 'Unit | Service | hpoinfo', {
    // Specify the other units that are required for this test.
    // needs: ['service:foo']
  });

  // Replace this with your real tests.
  (0, _emberQunit.test)('it exists', function (assert) {
    var service = this.subject();
    assert.ok(service);
  });
});
require('varwatch/tests/test-helper');
EmberENV.TESTS_FILE_LOADED = true;
//# sourceMappingURL=tests.map
