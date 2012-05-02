
window.fr = window.fr || {};
var fr = window.fr;

$(".date-value").change(function() {
  $('.export-button').addClass('hidden');
});

fr.summary_registrations_report = function(target) {
  var options = {
   chart: {
      renderTo: 'registrations',
      type: 'column'
   },
   title: {},
   xAxis: { 
    categories: [] 
   },
   yAxis: {
      min: 0,
      title: {}
   },
   tooltip: {
      formatter: function() {
        var s;
        if (this.point.name) { // the pie chart
          s = ''+ this.point.name +': '+ this.y;
        } else {
          s = ''+ this.series.name  +': '+ this.y;
        }
        return s;
      }
    },
  };

  $.getJSON(summaryregistrationsEndpoint, function(data) {
    options.title.text = data.title;
    options.xAxis.categories = data.categories;
    options.yAxis.title.text = data.axis.y;
    options.series = [];
    $.each(data.series, function(k, v) {
      var series;
      if(k == 'summary') {
        series = {
          type: 'spline',
          name: v.name,
          data: v.avg,
        }; 
      } else {
        series = {
          type: 'column',
          name: v.name,
          data: v.counts
        };  
      };
      options.series.push(series);
    });

    var registrations = new Highcharts.Chart(options);  
  });
};

fr.summary_subscriber_growth_report = function(target) {
  var options = {
    chart: {
      renderTo: target,
      type: 'area'
    },
    title: {},
    xAxis: {
      labels: {
        formatter: function() {
          return this.value; // clean, unformatted number for year
        }
      }
    },
    yAxis: {
      title: {},
      labels: {
        formatter: function() {
          return this.value;
        }
      }
    },
    plotOptions: {
      area: {
        stacking: 'normal',
        marker: {
          enabled: false,
          symbol: 'circle',
          radius: 2,
          states: {
            hover: {
              enabled: true
            }
          }
        }
      }
    }
  }

  $.getJSON(summarysubscribergrowthEndpoint, function(data) {
    options.title.text = data.title;
    options.xAxis.categories = data.categories;
    options.yAxis.title.text = data.axis.y;
    options.series = [];
    $.each(data.series, function(k, v) {
      var series = {
        name: v.name,
        data: v.counts
      };  
      options.series.push(series);
    });

    fedreg.hidelocalspinner(target);
    var growth = new Highcharts.Chart(options);
  });  
};

fr.summary_sessions_report = function(target) {
  var options = {
    chart: {
      renderTo: 'sessions',
      type: 'column'
    },
    title: {},
    xAxis: {
      labels: {
        formatter: function() {
          return this.value; // clean, unformatted number for year
        }
      }
    },
    yAxis: {
      title: {},
      labels: {
        formatter: function() {
          return this.value;
        }
      }
    },
    plotOptions: {
      area: {
        marker: {
          enabled: false,
          symbol: 'circle',
          radius: 2,
          states: {
            hover: {
              enabled: true
            }
          }
        }
      }
    }
  }

  $.getJSON(summarysessionsEndpoint, function(data) {
    options.title.text = data.title;
    options.xAxis.categories = data.categories;
    options.yAxis.title.text = data.axis.y;
    options.series = [];
    var series = {
      name: data.series.name,
      data: data.series.count
    };  
    options.series.push(series);

    var sessions = new Highcharts.Chart(options);
  });  
};

var detailedregistrations;
$(".export-detailed-registration-report").click(function() {
  var form = $('#detailed-registration-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    window.location = detailedregistrationsEndpoint + '?type=csv&' + params
  }
});
$(".request-detailed-registration-report").click(function () {
  var form = $('#detailed-registration-report-parameters');
  if(form.valid()) { 
    var exportBut = $('.export-detailed-registration-report');
    var registrationDetails = $("#registrationdetails");
    var organizationregistrations = $("#organizationregistrations");
    var idpregistrations = $("#idpregistrations");
    var spregistrations = $("#spregistrations");

    if(detailedregistrations) {
      detailedregistrations.destroy();
    }

    registrationDetails.addClass('hidden');
    organizationregistrations.html('');
    idpregistrations.html('');
    spregistrations.html('');
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedregistrationschart',
        type: 'area',
        height: 600,
        zoomType: 'x',
      },
      title: {},
      xAxis: {
        type: 'datetime',
        maxZoom: 14 * 24 * 3600000, // fourteen days
        title: {
          text: null
        }
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      plotOptions: {
        area: {
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var params = form.serialize();
    $.getJSON(detailedregistrationsEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      $.each(data.series, function(k, v) {
        var series = {
          type: 'area',
          pointInterval: 24 * 3600 * 1000,
          pointStart: Date.UTC(data.startdate.year, data.startdate.month, data.startdate.day),
          name: v.name,
          data: v.counts
        };  
        options.series.push(series);
      });

      $.each(data.detail.org, function(k,v) {
        organizationregistrations.append("<tr><td>"+v.displayName+"</td><td>"+v.dateCreated+"</td><td><a href='"+v.url+"' class='btn'>view</td></tr>");
      });

      $.each(data.detail.idp, function(k,v) {
        idpregistrations.append("<tr><td>"+v.displayName+"</td><td>"+v.dateCreated+"</td><td><a href='"+v.url+"' class='btn'>view</td></tr>");
      });

      $.each(data.detail.sp, function(k,v) {
        spregistrations.append("<tr><td>"+v.displayName+"</td><td>"+v.dateCreated+"</td><td><a href='"+v.url+"' class='btn'>view</td></tr>");
      });

      fedreg.hidespinner();
      detailedregistrations = new Highcharts.Chart(options);
      registrationDetails.removeClass('hidden');
      exportBut.removeClass('hidden');
    });
  }
});

var detailedsubscribergrowth;
$(".export-detailed-growth-report").click(function() {
  var form = $('#detailed-growth-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    window.location = detailedgrowthEndpoint + '?type=csv&' + params
  }
});
$(".request-detailed-growth-report").click(function () {
  var form = $('#detailed-growth-report-parameters');
  if(form.valid()) { 
    var exportBut = $('.export-detailed-growth-report');
    if(detailedsubscribergrowth)
      detailedsubscribergrowth.destroy();

    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedgrowthchart',
        type: 'area',
        height: 600,
        zoomType: 'x',
      },
      title: {},
      xAxis: {
        type: 'datetime',
        maxZoom: 14 * 24 * 3600000, // fourteen days
        title: {
          text: null
        }
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      plotOptions: {
        area: {
          stacking: 'normal',
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var params = form.serialize();
    $.getJSON(detailedgrowthEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      $.each(data.series, function(k, v) {
        var series = {
          type: 'area',
          pointInterval: 24 * 3600 * 1000,
          pointStart: Date.UTC(data.startdate.year, data.startdate.month, data.startdate.day),
          name: v.name,
          data: v.counts
        };  
        options.series.push(series);
      });

      fedreg.hidespinner();
      detailedsubscribergrowth = new Highcharts.Chart(options);
      exportBut.removeClass('hidden');
    });
  }
});

var detailedsessions;
$(".export-detailed-sessions-report").click(function() {
  var form = $('#detailed-sessions-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    window.location = detailedsessionsEndpoint + '?type=csv&' + params
  }
});
$(".request-detailed-sessions-report").click(function () {
  var form = $('#detailed-sessions-report-parameters');
  if(form.valid()) {
    var exportBut = $('.export-detailed-sessions-report');
    if(detailedsessions) {
      detailedsessions.destroy();
    }
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedsessionschart',
        type: 'area',
        height: 600,
        zoomType: 'x',
      },
      title: {},
      xAxis: {
        type: 'datetime',
        maxZoom: 14 * 24 * 3600000, // fourteen days
        title: {
          text: null
        }
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      plotOptions: {
        area: {
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var params = form.serialize();
    $.getJSON(detailedsessionsEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        type: 'area',
        pointInterval: 24 * 3600 * 1000,
        pointStart: Date.UTC(data.startdate.year, data.startdate.month, data.startdate.day),
        name: data.series.overall.name,
        data: data.series.overall.count
      };  
      options.series.push(series);


      fedreg.hidespinner();
      detailedsessions = new Highcharts.Chart(options);
      exportBut.removeClass('hidden');
    });
  }  
});

$(".select-all-topten-services").click(function () {
  $('#topten-utilized-services :unchecked').attr('checked', true);
  return false;
});

$(".unselect-all-topten-services").click(function () {
  $('#topten-utilized-services :checked').attr('checked', false);
  return false;
});

$(".select-all-remaining-services").click(function () {
  $('#remaning-utilized-services :unchecked').attr('checked', true);
  return false;
});

$(".unselect-all-remaining-services").click(function () {
  $('#remaning-utilized-services :checked').attr('checked', false);
  return false;
});

$(".request-refine-detailedserviceutilization-content").click(function () {
  fedreg.set_button($(this));
  var form = $('#detailed-detailedserviceutilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    params = params + '&' + $('#refine-detailedserviceutilization-report-parameters').serialize();
    requestServiceUtilization(params);
  }
  fedreg.reset_button($(this));
});

$(".request-detailed-detailedserviceutilization-reports").click(function () {
  var form = $('#detailed-detailedserviceutilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    requestServiceUtilization(params);
  }
});

$(".export-detailed-detailedserviceutilization-reports").click(function() {
  var form = $('#detailed-detailedserviceutilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    params = params + '&' + $('#refine-detailedserviceutilization-report-parameters').serialize();
    window.location = detailedserviceutilizationEndpoint + '?type=csv&' + params
  }
});

var serviceutilization;
var serviceutilizationtotals;
function requestServiceUtilization(params) {
    var refineContent = $("#refine-detailedserviceutilization-content");
    var topTenContent = $('#refine-detailedserviceutilization-report-parameters > .topten');
    var remainderContent = $('#refine-detailedserviceutilization-report-parameters > .remainder');
    var topTen = $('#topten-utilized-services');
    var remainder = $('#remaning-utilized-services');
    var exportBut = $('.export-detailed-detailedserviceutilization-reports');

    if(serviceutilization) {
      serviceutilization.destroy();
      serviceutilizationtotals.destroy();
    }

    refineContent.addClass('hidden');
    topTenContent.addClass('hidden');
    remainderContent.addClass('hidden');
    exportBut.addClass('hidden');
    topTen.html('');
    remainder.html(''); 
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedserviceutilization',
        defaultSeriesType: 'bar',
        height: 10
      },
      title: {},
      xAxis: {
        categories: [],
        title: {
          enabled:false
        },
      },
      yAxis: {
        min: 0,
        title: {
          text: '',
        }
      },
      legend: {
        enabled: false,
      },
      plotOptions: {
        bar: {
          dataLabels: {
              enabled: true,
              y:-5,
              color:"black",
              style: {
                  fontSize: "12px"
              },
              formatter: function(){
                return this.name;
              }
          }
        }
      }
    }

    var options2 = {
      chart: {
        renderTo: 'detailedserviceutilizationtotals',
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        height: 440
      },
      title: {
        text: ''
      },
      tooltip: {
        formatter: function() {
          return '<b>'+ this.point.name +'</b>: '+ this.y + ' sessions ('+this.percentage +' %)';
        }
      },
      plotOptions: {
        pie: {
          allowPointSelect: true,
          cursor: 'pointer',
          dataLabels: {
            enabled: false
          },
          showInLegend: false
        }
      },
      series: []
    }

    $.getJSON(detailedserviceutilizationEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        name: 'Sessions',
        data: [],
        showInLegend: true
      };
      
      if(data.series.length > 0) {
        topTenContent.removeClass('hidden');
        refineContent.removeClass('hidden');
        exportBut.removeClass('hidden');
      }
      else {
        topTenContent.addClass('hidden');
        refineContent.addClass('hidden');
        exportBut.addClass('hidden');
      }

      if(data.series.length > 10)
        remainderContent.removeClass('hidden');
      else
        remainderContent.addClass('hidden');

      var totals = {
        type: 'pie',
        name: 'Session Totals',
        size: 400,
        data: []
      };

      $.each(data.series, function(k, v) {
        if(!v.excluded) {
          options.chart.height = options.chart.height + 40 // allow room per rendered sp
          options.xAxis.categories.push(v.name);
          series.data.push(v.count);

          var data = {
            name: v.name,
            y: v.count
          };
          totals.data.push(data);

          var markup = '<label class="span3"><input name="activesp" type="checkbox" checked="checked" value="'+v.id+'"/> ' + v.name + '</label>';
        }
        else
          var markup = '<label class="span3"><input name="activesp" type="checkbox" value="'+v.id+'"/> ' + v.name + '</label>';

        if(k < 10)
          topTen.append(markup);
        else
          remainder.append(markup);
      });

      options.series.push(series); 
      options2.series.push(totals);
      
      fedreg.hidespinner();
      serviceutilization = new Highcharts.Chart(options);
      serviceutilizationtotals = new Highcharts.Chart(options2);
    });
};

$(".select-all-topten-idps").click(function () {
  $('#topten-utilized-idps :unchecked').attr('checked', true);
  return false;
});

$(".unselect-all-topten-idps").click(function () {
  $('#topten-utilized-idps :checked').attr('checked', false);
  return false;
});

$(".select-all-remaining-idps").click(function () {
  $('#remaning-utilized-idps :unchecked').attr('checked', true);
  return false;
});

$(".unselect-all-remaining-idps").click(function () {
  $('#remaning-utilized-idps :checked').attr('checked', false);
  return false;
});

$(".request-refine-detailedidputilization-content").click(function () {
  fedreg.set_button($(this));
  var form = $('#detailed-detailedidputilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    params = params + '&' + $('#refine-detailedidputilization-report-parameters').serialize();
    requestIdPUtilization(params);
  }
  fedreg.reset_button($(this));
});

$(".request-detailedidputilization-reports").click(function () {
  var form = $('#detailed-detailedidputilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    requestIdPUtilization(params);
  }
});

$(".export-detailedidputilization-reports").click(function() {
  var form = $('#detailed-detailedidputilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    params = params + '&' + $('#refine-detailedidputilization-report-parameters').serialize();
    window.location = detailedidputilizationEndpoint + '?type=csv&' + params
  }
});

var idputilization;
var idputilizationtotals;
function requestIdPUtilization(params) {
    var refineContent = $("#refine-detailedidputilization-content");
    var topTenContent = $('#refine-detailedidputilization-report-parameters > .topten');
    var remainderContent = $('#refine-detailedidputilization-report-parameters > .remainder');
    var topTen = $('#topten-utilized-idps');
    var remainder = $('#remaning-utilized-idps');
    var exportBut = $('.export-detailedidputilization-reports');

    if(idputilization) {
      idputilization.destroy();
      idputilizationtotals.destroy();
    }

    refineContent.addClass('hidden');
    topTenContent.addClass('hidden');
    remainderContent.addClass('hidden');
    exportBut.addClass('hidden');
    topTen.html('');
    remainder.html(''); 
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedidputilization',
        defaultSeriesType: 'bar',
        height: 15
      },
      title: {},
      xAxis: {
        categories: [],
        title: {
          enabled:false
        },
      },
      yAxis: {
        min: 0,
        title: {
          text: '',
        }
      },
      legend: {
        enabled: false,
      },
      plotOptions: {
        bar: {
          dataLabels: {
              enabled: true,
              y:-5,
              color:"black",
              style: {
                  fontSize: "12px"
              },
              formatter: function(){
                return this.name;
              }
          }
        }
      }
    }

    var options2 = {
      chart: {
        renderTo: 'detailedidputilizationtotals',
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        height: 440
      },
      title: {
        text: ''
      },
      tooltip: {
        formatter: function() {
          return '<b>'+ this.point.name +'</b>: '+ this.y + ' sessions ('+this.percentage +' %)';
        }
      },
      plotOptions: {
        pie: {
          allowPointSelect: true,
          cursor: 'pointer',
          dataLabels: {
            enabled: false
          },
          showInLegend: false
        }
      },
      series: []
    }

    $.getJSON(detailedidputilizationEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        name: 'Sessions',
        data: [],
        showInLegend: true
      };
      
      if(data.series.length > 0) {
        topTenContent.removeClass('hidden');
        refineContent.removeClass('hidden');
        exportBut.removeClass('hidden');
      }
      else {
        topTenContent.addClass('hidden');
        refineContent.removeClass('hidden');
      }

      if(data.series.length > 10)
        remainderContent.removeClass('hidden');
      else
        remainderContent.addClass('hidden');

      var totals = {
        type: 'pie',
        name: 'Session Totals',
        size: 400,
        data: []
      };

      $.each(data.series, function(k, v) {
        if(!v.excluded) {
          options.chart.height = options.chart.height + 40 // allow room per rendered sp
          options.xAxis.categories.push(v.name);
          series.data.push(v.count);

          var data = {
            name: v.name,
            y: v.count
          };
          totals.data.push(data);

          var markup = '<label class="span3"><input name="activeidp" type="checkbox" checked="checked" value="'+v.id+'"/> ' + v.name + '</label>';
        }
        else
          var markup = '<label class="span3"><input name="activeidp" type="checkbox" value="'+v.id+'"/> ' + v.name + '</label>';

        if(k < 10)
          topTen.append(markup);
        else
          remainder.append(markup);
      });

      options.series.push(series); 
      options2.series.push(totals);
      
      fedreg.hidespinner();
      idputilization = new Highcharts.Chart(options);
      idputilizationtotals = new Highcharts.Chart(options2);
    });
};

var detaileddemand;
$(".export-detailed-demand-report").click(function() {
  var form = $('#detailed-demand-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    window.location = detaileddemandEndpoint + '?type=csv&' + params
  }
});

$(".request-detailed-demand-report").click(function () {
  var form = $('#detailed-demand-report-parameters');
  if(form.valid()) {

    var exportBut = $('.export-detailed-demand-report');
    if(detaileddemand) {
      detaileddemand.destroy();
    }
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'demanddetailed',
        type: 'area',
        height: 600,
      },
      title: {},
      xAxis: {
        type: 'linear',
        title: {
          text: 'Hour in day (24 hr format)'
        },
        tickInterval: 1
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      legend: {
        enabled: false,
      },
      plotOptions: {
        area: {
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var params = form.serialize();
    $.getJSON(detaileddemandEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        type: 'area',
        name: 'sessions',
        color: '#3BA187',
        data: data.series
      };  
      options.series.push(series);


      fedreg.hidespinner();
      exportBut.removeClass('hidden');
      detaileddemand = new Highcharts.Chart(options);
    });
  }  
});

var dsutilizationaccesses;
var dsutilizationtotals;
$(".export-detailed-dsutilization-report").click(function() {
  var form = $('#detailed-dsutilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    window.location = detaileddsutilizationEndpoint + '?type=csv&' + params
  }
});
$(".request-detailed-dsutilization-reports").click(function () {
  var form = $('#detailed-dsutilization-report-parameters');
  if(form.valid()) { 
    var exportBut = $('.export-detailed-dsutilization-report');
    if(dsutilizationaccesses) {
      dsutilizationaccesses.destroy();
      dsutilizationtotals.destroy();
    }
    fedreg.showspinner();

    var options2 = {
      chart: {
        renderTo: 'detailedwayfnodesessions',
        type: 'area',
        height: 600,
        zoomType: 'x',
      },
      title: {},
      xAxis: {
        type: 'datetime',
        maxZoom: 14 * 24 * 3600000, // fourteen days
        title: {
          text: null
        }
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      plotOptions: {
        area: {
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var options3 = {
      chart: {
        renderTo: 'detailedwayfnodesessionstotals',
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: true,
        height: 460,
      },
      title: {
        text: ''
      },
      tooltip: {
        formatter: function() {
          return '<b>'+ this.point.name +'</b>: '+ this.y + ' sessions ('+this.percentage +' %)';
        }
      },
      plotOptions: {
        pie: {
          allowPointSelect: true,
          cursor: 'pointer',
          dataLabels: {
            enabled: false
          },
          showInLegend: true
        }
      },
      series: []
    }

    var params = form.serialize();
    $.getJSON(detaileddsutilizationEndpoint, params, function(data) {
      options2.title.text = data.title;
      options2.yAxis.title.text = data.axis.y;
      options2.series = [];
      $.each(data.series, function(k, v) {
        var series = {
          type: 'area',
          pointInterval: 24 * 3600 * 1000,
          pointStart: Date.UTC(data.startdate.year, data.startdate.month, data.startdate.day),
          name: v.name,
          data: v.counts
        };  
        options2.series.push(series);
      });

      var totals = {
        type: 'pie',
        name: 'Session Totals',
        size: 380,
        data: []
      };
      $.each(data.totals, function(k, v) {
        var data = {
          name: v.name,
          y: v.count
        };
        totals.data.push(data); 
      });
      options3.series.push(totals);

      fedreg.hidespinner();
      dsutilizationaccesses = new Highcharts.Chart(options2);
      dsutilizationtotals = new Highcharts.Chart(options3);
      exportBut.removeClass('hidden');
    });
  }  
});

// Compliance
fr.attributesupport_compliance_report = function(target) {
  fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'attributesupportchart',
        defaultSeriesType: 'bar',
        height: 15
      },
      title: {},
      xAxis: {
        categories: [],
        title: {
          enabled:false
        },
      },
      yAxis: {
        min: 0,
        title: {
          text: '',
        }
      },
      legend: {
        enabled: false,
      },
      plotOptions: {
        bar: {
          dataLabels: {
              enabled: true,
              y:-5,
              color:"black",
              style: {
                  fontSize: "12px"
              },
              formatter: function(){
                return this.name;
              }
          }
        }
      },
      tooltip: {
        formatter: function() {
          return ''+
            this.series.name +': '+ this.y +' supported';
        }
      },
    }

    $.getJSON(attributesupportEndpoint, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;

      $.each(data.categories, function(k, v){
        options.chart.height = options.chart.height + 40 // allow room per rendered sp
        options.xAxis.categories.push(v.name);
      });

      options.series = [];
      $.each(data.series, function(k, v) {
        var series = {
          max: v.max,
          name: k,
          data: v.data,
          showInLegend: true,
          tooltip: {
            formatter: function() {
              return this.y + '/' + v.max;
            },
          }
        };
        options.series.push(series);
      });
      
      options.series.reverse();
      fedreg.hidespinner();
      attributesupport = new Highcharts.Chart(options);
    });
};

fr.detailed_attributesupport_compliance_report = function(target) {
  var exportBut = $('.export-detailed-attributesupport-report');
  fedreg.showspinner();

    $.getJSON(attributesupportEndpoint, function(data) {

      $.each(data.categories, function(k, v){
        $('#choose-indepth-idp').append($('<option></option>').val('indepth-idp-'+k).html(v.name));
        var alert
        if(v.automatedRelease) {
          alert = '<div class="span6 alert alert-block alert-success alert-spacer"><h4 class="alert-heading">Automatic Release</h4>This IdP automatically supplies attributes requested by services which it <strong>shows as available</strong> below.</div>'
        } else {
          alert = '<div class="span6 alert alert-block alert-spacer"><h4 class="alert-heading">Warning!</h4>This IdP <strong>does not</strong> automatically supply attributes requested by services. Service administrators will need to contact this IdP if they wish to be compatible with it.</div>'
        }
        $('#attributesupporttables').append('<div id="indepth-idp-'+k+'" class="span8 hidden idp-indepth"><div class="row"><div class="span4"><h3>'+v.name+'</h3></div><div class="span2 offset2"><a href="'+v.url+'" class="btn">View</a></div></div>'+alert+'<div class="row"><div id="idp-'+k+'" class="span8"></div></div></div>');
      });

      $.each(data.series, function(k, v) {        
        $.each(v.data, function(l, w) {
          $('#idp-'+l).append('<div class="offset0"><h4>'+ k +' Attributes</h4></div>');

          $('#idp-'+l).append('<div class="offset1"><h5>Supported ('+w['supported'].length+')</h5></div>');
          if(w['supported'].length > 0) {
            $.each(w['supported'], function(m,x) {
              $('#idp-'+l).append('<div class="offset2">' + data.attributes[x] + '</div>');
            });
          } else {
            $('#idp-'+l).append('<div class="offset2"> N/A - None supported</div>');
          }
          $('#idp-'+l).append('<br>');
          $('#idp-'+l).append('<div class="offset1"><h5>Unsupported ('+w['unsupported'].length+')</h5></div>');
          if(w['unsupported'].length > 0) {
            $.each(w['unsupported'], function(m,x) {
              $('#idp-'+l).append('<div class="offset2">' + data.attributes[x] + '</div>');
            });
          } else {
            $('#idp-'+l).append('<div class="offset2"> N/A - All supported</div>');
          }
          $('#idp-'+l).append('<br>');
        });
      });
      
      fedreg.hidespinner();
      $('.detailed-idpattributesupport-report-parameters').removeClass('hidden');
      $('#attributesupporttables').removeClass('hidden');
      $(".detailed-idpattributesupport-report").click();
    });

};

$(".detailed-idpattributesupport-report").click(function() {
  $('.idp-indepth').addClass('hidden');
  $('#'+$('#choose-indepth-idp').attr('value')).removeClass('hidden');
});

$(".detailed-idpattributesupport-reports").click(function() {
  $('.idp-indepth').removeClass('hidden');
});

$(".export-detailed-idpattributesupport-report").click(function() {
  window.location = attributesupportEndpoint + '?type=csv'
});

$(".request-detailed-attributecompatibility-report").click(function(target) {
  $('#attributecompatibility').addClass('hidden');
  fedreg.showspinner();
  $('#attributecompatibility .alert').addClass('hidden');

  var form = $('#detailed-attributecompatibility-report-parameters');
  var params = form.serialize();

    $.getJSON(attributecompatibilityEndpoint, params, function(data) {
      if(!data.minimumRequirements) {
        $('.unsupportedattributes').removeClass('hidden');
      }
      if(!data.idp.automatedRelease) {
        $('.automaticrelease').removeClass('hidden');
      }
      if(data.idp.automatedRelease && data.minimumRequirements) {
        $('.releasesuccess').removeClass('hidden');
      }

      var req = $('#attributecompatibility .requiredattributes');
      req.html('');
      var op = $('#attributecompatibility .optionalattributes');
      op.html('');
      $.each(data.series.required, function(k,v) {
        var attr = '<div class="span1 offset1"> '+ v[0] + ' </div>';
        if(v[1])
          attr = attr + '<div class="span1 offset2"> supported </div>'
        else
          attr = attr + '<div class="span1 offset2 not-functioning"> unsupported </div>'
        req.append(attr);
      });

      $.each(data.series.optional, function(k,v) {
        var attr = '<div class="span1 offset1"> '+ v[0] + ' </div>';
        if(v[1])
          attr = attr + '<div class="span1 offset2"> supported </div>'
        else
          attr = attr + '<div class="span1 offset2 not-functioning"> unsupported </div>'
        op.append(attr);
      });

      fedreg.hidespinner();
      $('#attributecompatibility').removeClass('hidden');
    });
});

$(".request-idpprovidingattribute-report").click(function(target) {
  $('#idpprovidingattribute').addClass('hidden');
  fedreg.showspinner();

  var form = $('#idpprovidingattribute-report-parameters');
  var params = form.serialize();

    $.getJSON(idpprovidingattributeEndpoint, params, function(data) {
      $('.attributename').text(data.attribute);

      var supported = $("#idpprovidingattribute .supported");
      supported.html('');
      $('.supportedcount').html(data.supported.length);
      $.each(data.supported, function(k,v) {
        var markup = "<tr><td>"+v.name

        if(!v.automatedRelease)
          markup = markup + "<br><span class='label label-important'>No automated attribute release</span>";

        markup = markup + "</td><td><a href='"+v.url+"' class='btn'>View</a></td>";
        supported.append(markup);
      });

      var unsupported = $("#idpprovidingattribute .unsupported");
      unsupported.html('');
      $('.unsupportedcount').html(data.unsupported.length);
      $.each(data.unsupported, function(k,v) {
        var markup = "<tr><td>"+v.name

        if(!v.automatedRelease)
          markup = markup + "<br><span class='label label-important'>No automated attribute release</span>";

        markup = markup + "</td><td><a href='"+v.url+"' class='btn'>View</a></td>";
        unsupported.append(markup);
      });

      fedreg.hidespinner();
      $('#idpprovidingattribute').removeClass('hidden');
    });
});

// Identity Providers

var detailedidpsessions;
$(".export-detailed-idpsessions-report").click(function() {
  var form = $('#detailed-idpsessions-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    window.location = detailedidpsessionsEndpoint + '?type=csv&' + params
  }
});
$(".request-detailed-idpsessions-report").click(function () {
  var form = $('#detailed-idpsessions-report-parameters');
  if(form.valid()) {
    var exportBut = $('.export-detailed-idpsessions-report');
    if(detailedidpsessions) {
      detailedidpsessions.destroy();
    }
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedidpsessionschart',
        type: 'area',
        height: 600,
        zoomType: 'x',
      },
      title: {},
      xAxis: {
        type: 'datetime',
        maxZoom: 14 * 24 * 3600000, // fourteen days
        title: {
          text: null
        }
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      plotOptions: {
        area: {
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var params = form.serialize();
    $.getJSON(detailedidpsessionsEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        type: 'area',
        pointInterval: 24 * 3600 * 1000,
        pointStart: Date.UTC(data.startdate.year, data.startdate.month, data.startdate.day),
        name: data.series.overall.name,
        data: data.series.overall.count
      };  
      options.series.push(series);


      fedreg.hidespinner();
      detailedidpsessions = new Highcharts.Chart(options);
      exportBut.removeClass('hidden');
    });
  }  
});

$(".request-refine-detailedidptoserviceutilization-content").click(function () {
  fedreg.set_button($(this));
  var form = $('#detailed-detailedidptoserviceutilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    params = params + '&' + $('#refine-detailedidptoserviceutilization-report-parameters').serialize();
    requestIdPServiceUtilization(params);
  }
  fedreg.reset_button($(this));
});

$(".request-detailed-detailedidptoserviceutilization-reports").click(function () {
  var form = $('#detailed-detailedidptoserviceutilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    requestIdPServiceUtilization(params);
  }
});

$(".export-detailed-detailedidptoserviceutilization-reports").click(function() {
  var form = $('#detailed-detailedidptoserviceutilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    params = params + '&' + $('#refine-detailedidptoserviceutilization-report-parameters').serialize();
    window.location = detailedidptoserviceutilizationEndpoint + '?type=csv&' + params
  }
});

var idpserviceutilization;
var idpserviceutilizationtotals;
function requestIdPServiceUtilization(params) {
    var refineContent = $("#refine-detailedidptoserviceutilization-content");
    var topTenContent = $('#refine-detailedidptoserviceutilization-report-parameters > .topten');
    var remainderContent = $('#refine-detailedidptoserviceutilization-report-parameters > .remainder');
    var topTen = $('#topten-utilized-services');
    var remainder = $('#remaning-utilized-services');
    var exportBut = $('.export-detailed-detailedidptoserviceutilization-reports');

    if(idpserviceutilization) {
      idpserviceutilization.destroy();
      idpserviceutilizationtotals.destroy();
    }
    
    fedreg.showspinner();
    refineContent.addClass('hidden');
    topTenContent.addClass('hidden');
    remainderContent.addClass('hidden');
    exportBut.addClass('hidden');
    topTen.html('');
    remainder.html(''); 
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedidptoserviceutilization',
        defaultSeriesType: 'bar',
        height: 15
      },
      title: {},
      xAxis: {
        categories: [],
        title: {
          enabled:false
        },
      },
      yAxis: {
        min: 0,
        title: {
          text: '',
        }
      },
      legend: {
        enabled: false,
      },
      plotOptions: {
        bar: {
          dataLabels: {
              enabled: true,
              y:-5,
              color:"black",
              style: {
                  fontSize: "12px"
              },
              formatter: function(){
                return this.name;
              }
          }
        }
      }
    }

    var options2 = {
      chart: {
        renderTo: 'detailedidptoserviceutilizationtotals',
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        height: 440
      },
      title: {
        text: ''
      },
      tooltip: {
        formatter: function() {
          return '<b>'+ this.point.name +'</b>: '+ this.y + ' sessions ('+this.percentage +' %)';
        }
      },
      plotOptions: {
        pie: {
          allowPointSelect: true,
          cursor: 'pointer',
          dataLabels: {
            enabled: false
          },
          showInLegend: false
        }
      },
      series: []
    }

    $.getJSON(detailedidptoserviceutilizationEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        name: 'Sessions',
        data: [],
        showInLegend: true
      };
      
      if(data.series.length > 0) {
        topTenContent.removeClass('hidden');
        refineContent.removeClass('hidden');
        exportBut.removeClass('hidden');
      }
      else {
        topTenContent.addClass('hidden');
        refineContent.addClass('hidden');
        exportBut.addClass('hidden');
      }

      if(data.series.length > 10)
        remainderContent.removeClass('hidden');
      else
        remainderContent.addClass('hidden');

      var totals = {
        type: 'pie',
        name: 'Session Totals',
        size: 400,
        data: []
      };

      $.each(data.series, function(k, v) {
        if(!v.excluded) {
          options.chart.height = options.chart.height + 40 // allow room per rendered sp
          options.xAxis.categories.push(v.name);
          series.data.push(v.count);

          var data = {
            name: v.name,
            y: v.count
          };
          totals.data.push(data);

          var markup = '<label class="span3"><input name="activesp" type="checkbox" checked="checked" value="'+v.id+'"/> ' + v.name + '</label>';
        }
        else
          var markup = '<label class="span3"><input name="activesp" type="checkbox" value="'+v.id+'"/> ' + v.name + '</label>';

        if(k < 10)
          topTen.append(markup);
        else
          remainder.append(markup);
      });

      options.series.push(series); 
      options2.series.push(totals);
      
      fedreg.hidespinner();
      idpserviceutilization = new Highcharts.Chart(options);
      idpserviceutilizationtotals = new Highcharts.Chart(options2);
    });
};



var detailedidpdemand;
$(".export-detailed-idpdemand-report").click(function() {
  var form = $('#detailed-idpdemand-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    window.location = detailedidpdemandEndpoint + '?type=csv&' + params
  }
});

$(".request-detailed-idpdemand-report").click(function () {
  var form = $('#detailed-idpdemand-report-parameters');
  if(form.valid()) {

    var exportBut = $('.export-detailed-idpdemand-report');
    if(detailedidpdemand) {
      detailedidpdemand.destroy();
    }
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'idpdemanddetailed',
        type: 'area',
        height: 600,
      },
      title: {},
      xAxis: {
        type: 'linear',
        title: {
          text: 'Hour in day (24 hr format)'
        },
        tickInterval: 1
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      legend: {
        enabled: false,
      },
      plotOptions: {
        area: {
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var params = form.serialize();
    $.getJSON(detailedidpdemandEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        type: 'area',
        name: 'sessions',
        color: '#3BA187',
        data: data.series
      };  
      options.series.push(series);


      fedreg.hidespinner();
      exportBut.removeClass('hidden');
      detailedidpdemand = new Highcharts.Chart(options);
    });
  }  
});

// Service Providers

var detailedspsessions;
$(".export-detailed-spsessions-report").click(function() {
  var form = $('#detailed-spsessions-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    window.location = detailedspsessionsEndpoint + '?type=csv&' + params
  }
});
$(".request-detailed-spsessions-report").click(function () {
  var form = $('#detailed-spsessions-report-parameters');
  if(form.valid()) {
    var exportBut = $('.export-detailed-spsessions-report');
    if(detailedspsessions) {
      detailedspsessions.destroy();
    }
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedspsessionschart',
        type: 'area',
        height: 600,
        zoomType: 'x',
      },
      title: {},
      xAxis: {
        type: 'datetime',
        maxZoom: 14 * 24 * 3600000, // fourteen days
        title: {
          text: null
        }
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      plotOptions: {
        area: {
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var params = form.serialize();
    $.getJSON(detailedspsessionsEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        type: 'area',
        pointInterval: 24 * 3600 * 1000,
        pointStart: Date.UTC(data.startdate.year, data.startdate.month, data.startdate.day),
        name: data.series.overall.name,
        data: data.series.overall.count
      };  
      options.series.push(series);


      fedreg.hidespinner();
      detailedspsessions = new Highcharts.Chart(options);
      exportBut.removeClass('hidden');
    });
  }  
});

$(".request-refine-detailedsptoidputilization-content").click(function () {
  fedreg.set_button($(this));
  var form = $('#detailed-detailedsptoidputilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    params = params + '&' + $('#refine-detailedsptoidputilization-report-parameters').serialize();
    requestSptoIdPUtilization(params);
  }
  fedreg.reset_button($(this));
});

$(".request-detailed-detailedsptoidputilization-reports").click(function () {
  var form = $('#detailed-detailedsptoidputilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    requestSptoIdPUtilization(params);
  }
});

$(".export-detailed-detailedsptoidputilization-reports").click(function() {
  var form = $('#detailed-detailedsptoidputilization-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    params = params + '&' + $('#refine-detailedsptoidputilization-report-parameters').serialize();
    window.location = detailedsptoidputilizationEndpoint + '?type=csv&' + params
  }
});

var spserviceutilization;
var spserviceutilizationtotals;
function requestSptoIdPUtilization(params) {
    var refineContent = $("#refine-detailedsptoidputilization-content");
    var topTenContent = $('#refine-detailedsptoidputilization-report-parameters > .topten');
    var remainderContent = $('#refine-detailedsptoidputilization-report-parameters > .remainder');
    var topTen = $('#topten-utilized-services');
    var remainder = $('#remaning-utilized-services');
    var exportBut = $('.export-detailed-detailedsptoidputilization-reports');

    if(spserviceutilization) {
      spserviceutilization.destroy();
      spserviceutilizationtotals.destroy();
    }

    refineContent.addClass('hidden');
    topTenContent.addClass('hidden');
    remainderContent.addClass('hidden');
    exportBut.addClass('hidden');
    topTen.html('');
    remainder.html(''); 
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedsptoidputilization',
        defaultSeriesType: 'bar',
        height: 15
      },
      title: {},
      xAxis: {
        categories: [],
        title: {
          enabled:false
        },
      },
      yAxis: {
        min: 0,
        title: {
          text: '',
        }
      },
      legend: {
        enabled: false,
      },
      plotOptions: {
        bar: {
          dataLabels: {
              enabled: true,
              y:-5,
              color:"black",
              style: {
                  fontSize: "12px"
              },
              formatter: function(){
                return this.name;
              }
          }
        }
      }
    }

    var options2 = {
      chart: {
        renderTo: 'detailedsptoidputilizationtotals',
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        height: 440
      },
      title: {
        text: ''
      },
      tooltip: {
        formatter: function() {
          return '<b>'+ this.point.name +'</b>: '+ this.y + ' sessions ('+this.percentage +' %)';
        }
      },
      plotOptions: {
        pie: {
          allowPointSelect: true,
          cursor: 'pointer',
          dataLabels: {
            enabled: false
          },
          showInLegend: false
        }
      },
      series: []
    }

    $.getJSON(detailedsptoidputilizationEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        name: 'Sessions',
        data: [],
        showInLegend: true
      };
      
      if(data.series.length > 0) {
        topTenContent.removeClass('hidden');
        refineContent.removeClass('hidden');
        exportBut.removeClass('hidden');
      }
      else {
        topTenContent.addClass('hidden');
        refineContent.addClass('hidden');
        exportBut.addClass('hidden');
      }

      if(data.series.length > 10)
        remainderContent.removeClass('hidden');
      else
        remainderContent.addClass('hidden');

      var totals = {
        type: 'pie',
        name: 'Session Totals',
        size: 400,
        data: []
      };

      $.each(data.series, function(k, v) {
        if(!v.excluded) {
          options.chart.height = options.chart.height + 40 // allow room per rendered sp
          options.xAxis.categories.push(v.name);
          series.data.push(v.count);

          var data = {
            name: v.name,
            y: v.count
          };
          totals.data.push(data);

          var markup = '<label class="span3"><input name="activeidp" type="checkbox" checked="checked" value="'+v.id+'"/> ' + v.name + '</label>';
        }
        else
          var markup = '<label class="span3"><input name="activeidp" type="checkbox" value="'+v.id+'"/> ' + v.name + '</label>';

        if(k < 10)
          topTen.append(markup);
        else
          remainder.append(markup);
      });

      options.series.push(series); 
      options2.series.push(totals);
      
      fedreg.hidespinner();
      spserviceutilization = new Highcharts.Chart(options);
      spserviceutilizationtotals = new Highcharts.Chart(options2);
    });
};



var detailedspdemand;
$(".export-detailed-spdemand-report").click(function() {
  var form = $('#detailed-spdemand-report-parameters');
  if(form.valid()) { 
    var params = form.serialize();
    window.location = detailedspdemandEndpoint + '?type=csv&' + params
  }
});

$(".request-detailed-spdemand-report").click(function () {
  var form = $('#detailed-spdemand-report-parameters');
  if(form.valid()) {

    var exportBut = $('.export-detailed-spdemand-report');
    if(detailedspdemand) {
      detailedspdemand.destroy();
    }
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'spdemanddetailed',
        type: 'area',
        height: 600,
      },
      title: {},
      xAxis: {
        type: 'linear',
        title: {
          text: 'Hour in day (24 hr format)'
        },
        tickInterval: 1
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      legend: {
        enabled: false,
      },
      plotOptions: {
        area: {
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var params = form.serialize();
    $.getJSON(detailedspdemandEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        type: 'area',
        name: 'sessions',
        color: '#3BA187',
        data: data.series
      };  
      options.series.push(series);


      fedreg.hidespinner();
      exportBut.removeClass('hidden');
      detailedspdemand = new Highcharts.Chart(options);
    });
  }  
});

var detailedfrsessions;
$(".request-detailed-frsessions-report").click(function () {
  var form = $('#detailed-frsessions-report-parameters');
  if(form.valid()) {
    if(detailedfrsessions) {
      detailedfrsessions.destroy();
    }
    fedreg.showspinner();

    var options = {
      chart: {
        renderTo: 'detailedfrsessionschart',
        type: 'area',
        height: 600,
        zoomType: 'x',
      },
      title: {},
      xAxis: {
        type: 'datetime',
        maxZoom: 14 * 24 * 3600000, // fourteen days
        title: {
          text: null
        }
      },
      yAxis: {
        title: {},
        labels: {
          formatter: function() {
            return this.value;
          }
        }
      },
      plotOptions: {
        area: {
          marker: {
            enabled: false,
            symbol: 'circle',
            radius: 2,
            states: {
              hover: {
                enabled: true
              }
            }
          }
        }
      }
    }

    var params = form.serialize();
    $.getJSON(detailedfrsessionsEndpoint, params, function(data) {
      options.title.text = data.title;
      options.yAxis.title.text = data.axis.y;
      options.series = [];
      var series = {
        type: 'area',
        pointInterval: 24 * 3600 * 1000,
        pointStart: Date.UTC(data.startdate.year, data.startdate.month, data.startdate.day),
        name: data.series.overall.name,
        data: data.series.overall.count
      };  
      options.series.push(series);


      fedreg.hidespinner();
      detailedfrsessions = new Highcharts.Chart(options);
    });
  }  
});