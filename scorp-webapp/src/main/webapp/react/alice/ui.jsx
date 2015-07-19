
var Button = React.createClass({
    render: function() {
        return <input type="button" className="ui-button ui-button-mblue" {...this.props} value={this.props.value}/>;
    }
});

var ButtonSmall = React.createClass({
    render: function() {
        return <Button {...this.props} className="ui-button ui-button-sblue" />
    }
});

var ButtonLarge = React.createClass({
    render: function() {
        return <Button {...this.props} className="ui-button ui-button-lblue" />
    }
});

var ButtonWhite = React.createClass({
    render: function() {
        return <Button {...this.props} className="ui-button ui-button-mwhite" />
    }
});

var ButtonSmallWhite = React.createClass({
    render: function() {
        return <Button {...this.props} className="ui-button ui-button-swhite" />
    }
});

var ButtonLargeWhite = React.createClass({
   render: function() {
       return <Button {...this.props} className="ui-button ui-button-lwhite" />
   }
});

var ButtonOrange = React.createClass({
    render: function() {
        return <Button {...this.props} className="ui-button ui-button-morange" />
    }
});

var ButtonSmallOrange = React.createClass({
    render: function() {
        return <Button {...this.props} className="ui-button ui-button-sorange" />
    }
});

var ButtonLargeOrange = React.createClass({
    render: function() {
        return <Button {...this.props} className="ui-button ui-button-lorange" />
    }
});

var Input = React.createClass({
    render: function() {
        return <input {...this.props} type="text" class="ui-input" />
    }
});

var InputEmail = React.createClass({
    render: function() {
        return <input {...this.props} type="email" class="ui-input" />
    }
});

var InputNumber = React.createClass({
    render: function() {
        return <input {...this.props} type="number" class="ui-input" />
    }
});

var InputPassword = React.createClass({
    render: function() {
        return <input {...this.props} type="password" class="ui-input" />
    }
});

var InputFile = React.createClass({
    render: function() {
        return <input {...this.props} type="file" class="ui-input" />
    }
});

var InputHidden = React.createClass({
    render: function() {
        return <input {...this.props} type="hidden" class="fn-hide" />
    }
});

var InputDate = React.createClass({
    render: function() {
        return <input {...this.props} type="date" class="ui-input" />
    }
});

var InputUrl= React.createClass({
    render: function() {
        return <input {...this.props} type="url" class="ui-input" />
    }
});

var InputTel= React.createClass({
    render: function() {
        return <input {...this.props} type="tel" class="ui-input" />
    }
});

var InputSearch= React.createClass({
    render: function() {
        return <input {...this.props} type="search" class="ui-input" />
    }
});

var InputSubmit= React.createClass({
    render: function() {
        return <input value="确定" {...this.props} type="submit" class="ui-button ui-button-morange" />
    }
});

var InputReset = React.createClass({
    render: function() {
        return <input value="取消" {...this.props} type="reset" class="ui-button ui-button-mwhite" />
    }
});

var Checkbox = React.createClass({
    render: function() {
        return <input {...this.props} type="checkbox" class="ui-checkbox" />
    }
});

var Radio = React.createClass({
    render: function() {
        return <input {...this.props} type="radio" class="ui-radio" />
    }
});

var Label = React.createClass({
    render: function() {
        return <label className="ui-label">${this.props.value}</label>
    }
});