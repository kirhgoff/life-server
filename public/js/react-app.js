/** @jsx React.DOM */

(function () {
    var LifeWindow = React.createClass({
        render: function() {
            return <pre></pre>;
        }
    });

    var GenerateButton = React.createClass({
        handleSubmit: function () {
            var msg = { 
                width: this.refs.width.getDOMNode().value, 
                width: this.refs.width.getDOMNode().value
            };
            console.log(msg)
            $.ajax({
                url: "/generateNewWorld", 
                type: "POST", data: 
                JSON.stringify(msg),
                contentType:"application/json; charset=utf-8", 
                dataType:"json",
                success: function (data){console.log("success:", data);},
                error: function (XMLHttpRequest, textStatus, errorThrown){console.log("error status:", textStatus, "error:", errorThrown);}
            });
            return false;
        },
        render: function () { return (
            <div id="footer">
                <form onSubmit={this.handleSubmit}>
                    <input type="text" id="width" ref="width" placeholder="Width..." className="input-block-level" />                
                    <input type="text" id="height" ref="height" placeholder="Height..." className="input-block-level" />
                    <input type="button" className="btn btn-primary" value="Submit" onClick={this.handleSubmit} />
                </form>
            </div>
            );}
    });

    var LifeMonitor = React.createClass({
        render: function () { return (
            <div>
                <LifeWindow/>
                <GenerateButton name="Generate" />
            </div>
            );}
    });

    console.log ("React:", React);
    console.log ("Document:", document);

    React.render(<LifeMonitor />, document.getElementById('life-monitor'));
})();
