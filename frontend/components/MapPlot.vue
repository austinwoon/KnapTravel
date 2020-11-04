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
            styleMap(feature){
                const year = feature.properties.datelisted
                    ? parseInt(feature.properties.datelisted.slice(0, 4))
                    : 0;
                const color = year > 2000 ? "red" : "blue";
                return { color: color };
            },

            onEachFeature(feature, layer) {
                if (feature.properties && feature.properties.name) {
                    layer.bindPopup(feature.properties.name);
                    layer.on('mouseover', () => { layer.openPopup(); });
                    layer.on('mouseout', () => { layer.closePopup(); });
                }
            },
            
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

                this.plotPoints.forEach(point => {
                    L.marker(point.coordinates, {
                        title: point.title,
                        riseOnHover: true,
                    }).addTo(mapDiv);
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