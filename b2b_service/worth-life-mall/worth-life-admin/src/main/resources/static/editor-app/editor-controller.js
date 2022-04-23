/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
'use strict';

angular.module('activitiModeler')
.controller('EditorUnsavedChangesPopupCrtl', ['$rootScope', '$scope', '$http', '$location', '$window', function ($rootScope, $scope, $http, $location, $window) {

	$scope.ok = function () {
		if ($scope.handleResponseFunction) {
			$scope.handleResponseFunction(true);

            // Also clear any 'onbeforeunload', added by oryx
            $window.onbeforeunload = undefined;
		}
		$scope.$hide();
	};

	$scope.cancel = function () {
		if ($scope.handleResponseFunction) {
			$scope.handleResponseFunction(false);
		}
        $scope.$hide();
	};
}]);

activitiModule
.directive('autoFocus', ['$timeout', '$parse', function($timeout, $parse) {
    return {
        restrict: 'AC',
        compile: function($element, attr) {

            return function(_scope, _element, _attrs) {
                var firstChild = (_attrs.focusFirstChild !== undefined);
                $timeout(function () {
                    if (firstChild) {
                        // look for first input-element in child-tree and focus that
                        var inputs = _element.find('input');
                        if (inputs && inputs.length > 0) {
                            inputs[0].focus();
                        }
                    } else {
                        // Focus element where the directive is put on
                        _element[0].focus();
                    }
                }, 100);
            }
        }
    };
}]);
