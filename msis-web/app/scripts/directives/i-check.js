(function(){
    'use strick';
    angular.module('webappApp')
        .directive('iCheck', ['$timeout', function($timeout) {
            return {
                require: 'ngModel',
                link: function ($scope, element, $attrs, ngModel) {
                    return $timeout(function () {
                        var value = $attrs.value;
                        var checked = $attrs.checked;
                        var $element = $(element);

                        // Instantiate the iCheck control.                            
                        $element.iCheck({
                            checkboxClass: 'icheckbox_square-green',
                            radioClass: 'iradio_square-green',
                            increaseArea: '20%'
                        });

                        // If the model changes, update the iCheck control.
                        $scope.$watch($attrs.ngModel, function (newValue) {
                            $element.iCheck('update');
                        });

                        // If the iCheck control changes, update the model.
                        $element.on('ifChanged', function (event) {
                            if ($element.attr('type') === 'radio' && $attrs.ngModel) {
                                $scope.$apply(function () {
                                    ngModel.$setViewValue(value);
                                });
                            }
                        });

                    });
                }
            };
        }]);
})();