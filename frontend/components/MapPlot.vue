<template>
  <div id="container">
    <div :id="this.mapContainerId" class="map-style"></div>
  </div>
</template>

<script>
    import "leaflet/dist/leaflet.css";
    import L from "leaflet";

    export default {
        name: "MapPlot",
        data() {
            return {}
        },
        props: {id: String, plotPoints: Array},
        computed: {
            mapContainerId() {
                return "mapContainer-" + this.id;
            },
            center() {
                const sumLat = this.plotPoints.reduce((accum, currentVal) => accum + currentVal.coordinates[0], 0);
                const sumLng = this.plotPoints.reduce((accum, currentVal) => accum + currentVal.coordinates[1], 0);
                return [sumLat / this.plotPoints.length, sumLng / this.plotPoints.length];
            }
        },
        methods: {
            setupLeafletMap() {
                const mapDiv = L.map(this.mapContainerId, {center: this.center, zoom: 12});
                L.tileLayer(
                    "https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}",
                    {
                        attribution:
                            'Map data (c) <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery (c) <a href="https://www.mapbox.com/">Mapbox</a>',
                        maxZoom: 18,
                        id: "mapbox/streets-v11",
                        accessToken: process.env.MAPBOX_API_KEY
                    }
                ).addTo(mapDiv);
                
                const coordinates = this.plotPoints.map(point => point.coordinates);
                
                
                L.polyline(coordinates, {
                    color: 'blue'
                }).addTo(mapDiv);
                

                this.plotPoints.forEach((point, i) => {
                    const options = {
                        opacity: 1.0
                    };
                    
                    if (i === 0 ) {
                        options.icon = L.icon({iconUrl: '/start.svg', iconSize: [36, 36]})
                    }
                    
                    if (i === this.plotPoints.length - 1) {
                        options.icon = L.icon({iconUrl: '/flag.svg', iconSize: [36, 36]})
                    }
                    
                    L.marker(point.coordinates, options
                    ).bindTooltip(point.name, {
                        direction: 'top',
                        permanent: true,
                        offset: [-15, -10]}).addTo(mapDiv)
                })
            }
        },
        mounted() {
            this.setupLeafletMap();
        }
    }
</script>

<style scoped>
  .map-style {
    width: 100%;
    height: 500px;
  }
</style>