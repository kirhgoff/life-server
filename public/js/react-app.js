/** @jsx React.DOM */

(function () {
    var LifeWindow = React.createClass({
        getInitialState: function () {
            return { result: ""};
        },
        render: function() {
            return <hr></hr><pre>{this.state.result}</pre><hr></hr>;
        }
    });

    var GenerateButton = React.createClass({
        handleSubmit: function () {
            var msg = { 
                width: this.refs.width.getDOMNode().value, 
                height: this.refs.height.getDOMNode().value
            };
            $.ajax({
                url: "/generateNewWorld", 
                type: "POST", data: 
                JSON.stringify(msg),
                contentType:"application/json; charset=utf-8", 
                dataType:"json",
                success: this.updateModel,
                error: function (XMLHttpRequest, textStatus, errorThrown){console.log("error status:", textStatus, "error:", errorThrown);}
            });
        },
        updateModel: function(data) {
            console.log ("Data:", data);
            this.setState({result:data});
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

    React.render(<LifeMonitor />, document.getElementById('life-monitor'));
})();
