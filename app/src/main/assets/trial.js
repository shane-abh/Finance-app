function init(date, bal){
    console.log((date));
    console.log(bal);
//    for(let i = 0;i<symbol.length;i++){
//        console.log(symbol[i]);
//    }
    var dps = [];

    var chart = new CanvasJS.Chart("chartContainer", {
        animationEnabled: true,
        theme: "light2",
        title:{
            text: "Simple Line Chart"
        },
        data: [{
                  		type: "line",

                      dataPoints: dps
                  	}]
    });
    for(let i=0;i<date.length;i++){
         dps.push({x:new Date(date[i]),y:parseFloat(bal[i])});
    }
    chart.render();

}