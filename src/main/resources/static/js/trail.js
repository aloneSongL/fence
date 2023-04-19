function userLocation() {
    $.ajax({
        url: '/location/add/' + locationId,
        type: 'post',
        traditional: true,
        data: {

        }
    })
}