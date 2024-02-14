var form = new Vue({
    el: '#form',
    data: {
        simulationDTO: JSON.parse(document.getElementById('simulationDTOJson').getAttribute('data-simulation-dto')),
        showTableHost: true,
        showTableCloudlet: true,
        showTableVm: true
    },
    methods: {
        submitForm: function () {
            console.log(this.simulationDTO)
            var formData = JSON.stringify(this.simulationDTO);
            var csrfToken = document.querySelector('input[name="_csrf"]').value;

            // Отправка данных на сервер с помощью Axios
            axios.post('/simulation/start', formData, {
                headers: {
                    'X-CSRF-TOKEN': csrfToken,
                    'Content-Type': 'application/json'
                }
            })
                .then(function (response) {
                    // Обработка успешного ответа от сервера
                    console.log(response.data);
                })
                .catch(function (error) {
                    // Обработка ошибки
                    console.error(error);
                });
        },

        addHost: function () {
            if (!this.showTableHost){
                this.showTableHost = true
            }
            var newHost = {
                hostPes: '',
                hostMips: '',
                hostRam: '',
                hostBw: '',
                hostStorage: ''
            };
            this.simulationDTO.hostDTOS.push(newHost);
        },
        removeHost: function (index) {
            this.simulationDTO.hostDTOS.splice(index, 1);
            if (this.simulationDTO.hostDTOS.length === 0) {
                this.showTableHost = false;
            }
        },



        addCloudlet: function () {
            if (!this.showTableCloudlet){
                this.showTableCloudlet = true
            }
            var newCloudlet = {
                cloudletPes: '',
                cloudletLength: '',
                cloudletSize: ''
            };
            this.simulationDTO.cloudletDTOS.push(newCloudlet);
        },
        removeCloudlet: function (index) {
            this.simulationDTO.cloudletDTOS.splice(index, 1);
            if (this.simulationDTO.cloudletDTOS.length === 0) {
                this.showTableCloudlet = false;
            }
        },

        addVm: function () {
            if (!this.showTableVm){
                this.showTableVm = true
            }
            var newVm = {
                vmPes: '',
                vmRam: '',
                vmBw: '',
                vmStorage: ''
            };
            this.simulationDTO.vmDTOS.push(newVm);
        },
        removeVm: function (index) {
            this.simulationDTO.vmDTOS.splice(index, 1);
            if (this.simulationDTO.vmDTOS.length === 0) {
                this.showTableVm = false;
            }
        }
    },
    mounted: function () {
        // После загрузки компонента добавим пустые объекты в списки

        this.simulationDTO.hostDTOS.push({
            hostPes: 8,
            hostMips: 1000,
            hostRam: 2048,
            hostBw: 10000,
            hostStorage: 1000000
        });
        this.simulationDTO.cloudletDTOS.push({
            cloudletPes: 2,
            cloudletLength: 10000,
            cloudletSize: 1024
        });
        this.simulationDTO.cloudletDTOS.push({
            cloudletPes: 2,
            cloudletLength: 10000,
            cloudletSize: 1024
        });
        this.simulationDTO.cloudletDTOS.push({
            cloudletPes: 2,
            cloudletLength: 10000,
            cloudletSize: 1024
        });
        this.simulationDTO.cloudletDTOS.push({
            cloudletPes: 2,
            cloudletLength: 10000,
            cloudletSize: 1024
        });
        this.simulationDTO.vmDTOS.push({
            vmPes: 4,
            vmRam: 512,
            vmBw: 1000,
            vmStorage: 10000
        });
        this.simulationDTO.vmDTOS.push({
            vmPes: 4,
            vmRam: 512,
            vmBw: 1000,
            vmStorage: 10000
        });
    }
});
