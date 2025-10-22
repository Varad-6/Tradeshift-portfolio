import React, { useState, useEffect } from 'react';
import {
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  Box,
  Button,
  Avatar,
  Chip,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Tabs,
  Tab,
  LinearProgress,
  Divider
} from '@mui/material';
import {
  TrendingUp,
  TrendingDown,
  AccountBalance,
  ShowChart,
  Add,
  Settings,
  Logout,
  Person,
  Security,
  Email,
  Phone,
  AttachMoney,
  PieChart,
  Timeline,
  Assessment,
  Notifications,
  Search,
  FilterList
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';

const PortfolioDashboard = () => {
  const { user, logout } = useAuth();
  const [activeTab, setActiveTab] = useState(0);
  const [showProfileDialog, setShowProfileDialog] = useState(false);
  const [showBrokerageDialog, setShowBrokerageDialog] = useState(false);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  // Mock data for portfolio (replace with real API calls)
  const [portfolioData] = useState({
    totalValue: 125000,
    dayChange: 2500,
    dayChangePercent: 2.04,
    totalGainLoss: 15000,
    totalGainLossPercent: 13.64,
    cashBalance: 5000,
    investedAmount: 120000
  });

  const [holdings] = useState([
    { symbol: 'AAPL', name: 'Apple Inc.', shares: 50, price: 175.50, value: 8775, change: 2.5, changePercent: 1.45 },
    { symbol: 'GOOGL', name: 'Alphabet Inc.', shares: 25, price: 142.30, value: 3557.5, change: -1.2, changePercent: -0.84 },
    { symbol: 'MSFT', name: 'Microsoft Corp.', shares: 30, price: 380.25, value: 11407.5, change: 5.8, changePercent: 1.55 },
    { symbol: 'TSLA', name: 'Tesla Inc.', shares: 15, price: 245.80, value: 3687, change: -3.2, changePercent: -1.28 },
    { symbol: 'AMZN', name: 'Amazon.com Inc.', shares: 20, price: 155.40, value: 3108, change: 1.8, changePercent: 1.17 }
  ]);

  const [watchlist] = useState([
    { symbol: 'NVDA', name: 'NVIDIA Corp.', price: 485.20, change: 12.5, changePercent: 2.64 },
    { symbol: 'META', name: 'Meta Platforms Inc.', price: 320.15, change: -2.1, changePercent: -0.65 },
    { symbol: 'NFLX', name: 'Netflix Inc.', price: 425.80, change: 8.3, changePercent: 1.99 }
  ]);

  const [recentTrades] = useState([
    { date: '2024-01-15', type: 'BUY', symbol: 'AAPL', shares: 10, price: 175.50, total: 1755 },
    { date: '2024-01-14', type: 'SELL', symbol: 'GOOGL', shares: 5, price: 142.30, total: 711.5 },
    { date: '2024-01-13', type: 'BUY', symbol: 'MSFT', shares: 15, price: 380.25, total: 5703.75 }
  ]);

  const handleTabChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  const handleLogout = () => {
    logout();
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  const formatPercent = (percent) => {
    return `${percent >= 0 ? '+' : ''}${percent.toFixed(2)}%`;
  };

  const getChangeColor = (change) => {
    return change >= 0 ? 'success.main' : 'error.main';
  };

  const getChangeIcon = (change) => {
    return change >= 0 ? <TrendingUp /> : <TrendingDown />;
  };

  const renderOverviewTab = () => (
    <Grid container spacing={3}>
      {/* Portfolio Summary Cards */}
      <Grid item xs={12} md={3}>
        <Card sx={{ height: '100%' }}>
          <CardContent>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Avatar sx={{ bgcolor: 'primary.main', mr: 2 }}>
                <AttachMoney />
              </Avatar>
              <Box>
                <Typography variant="h6">Total Portfolio</Typography>
                <Typography variant="h4" color="primary">
                  {formatCurrency(portfolioData.totalValue)}
                </Typography>
              </Box>
            </Box>
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              {getChangeIcon(portfolioData.dayChange)}
              <Typography 
                variant="body2" 
                color={getChangeColor(portfolioData.dayChange)}
                sx={{ ml: 1 }}
              >
                {formatCurrency(portfolioData.dayChange)} ({formatPercent(portfolioData.dayChangePercent)})
              </Typography>
            </Box>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} md={3}>
        <Card sx={{ height: '100%' }}>
          <CardContent>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Avatar sx={{ bgcolor: 'success.main', mr: 2 }}>
                <TrendingUp />
              </Avatar>
              <Box>
                <Typography variant="h6">Total Gain/Loss</Typography>
                <Typography variant="h4" color="success.main">
                  {formatCurrency(portfolioData.totalGainLoss)}
                </Typography>
              </Box>
            </Box>
            <Typography variant="body2" color="text.secondary">
              {formatPercent(portfolioData.totalGainLossPercent)} return
            </Typography>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} md={3}>
        <Card sx={{ height: '100%' }}>
          <CardContent>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Avatar sx={{ bgcolor: 'info.main', mr: 2 }}>
                <AccountBalance />
              </Avatar>
              <Box>
                <Typography variant="h6">Cash Balance</Typography>
                <Typography variant="h4" color="info.main">
                  {formatCurrency(portfolioData.cashBalance)}
                </Typography>
              </Box>
            </Box>
            <Typography variant="body2" color="text.secondary">
              Available for trading
            </Typography>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} md={3}>
        <Card sx={{ height: '100%' }}>
          <CardContent>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Avatar sx={{ bgcolor: 'warning.main', mr: 2 }}>
                <PieChart />
              </Avatar>
              <Box>
                <Typography variant="h6">Invested Amount</Typography>
                <Typography variant="h4" color="warning.main">
                  {formatCurrency(portfolioData.investedAmount)}
                </Typography>
              </Box>
            </Box>
            <Typography variant="body2" color="text.secondary">
              Total invested capital
            </Typography>
          </CardContent>
        </Card>
      </Grid>

      {/* Holdings Table */}
      <Grid item xs={12}>
        <Card>
          <CardContent>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6">Current Holdings</Typography>
              <Button variant="outlined" startIcon={<Add />}>
                Add Position
              </Button>
            </Box>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Symbol</TableCell>
                    <TableCell>Name</TableCell>
                    <TableCell align="right">Shares</TableCell>
                    <TableCell align="right">Price</TableCell>
                    <TableCell align="right">Value</TableCell>
                    <TableCell align="right">Change</TableCell>
                    <TableCell align="right">Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {holdings.map((holding) => (
                    <TableRow key={holding.symbol}>
                      <TableCell>
                        <Typography variant="subtitle2" fontWeight="bold">
                          {holding.symbol}
                        </Typography>
                      </TableCell>
                      <TableCell>{holding.name}</TableCell>
                      <TableCell align="right">{holding.shares}</TableCell>
                      <TableCell align="right">{formatCurrency(holding.price)}</TableCell>
                      <TableCell align="right">{formatCurrency(holding.value)}</TableCell>
                      <TableCell align="right">
                        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
                          {getChangeIcon(holding.change)}
                          <Typography 
                            variant="body2" 
                            color={getChangeColor(holding.change)}
                            sx={{ ml: 0.5 }}
                          >
                            {formatPercent(holding.changePercent)}
                          </Typography>
                        </Box>
                      </TableCell>
                      <TableCell align="right">
                        <IconButton size="small">
                          <Settings />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );

  const renderWatchlistTab = () => (
    <Grid container spacing={3}>
      <Grid item xs={12}>
        <Card>
          <CardContent>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6">Watchlist</Typography>
              <Box>
                <TextField
                  size="small"
                  placeholder="Search symbols..."
                  InputProps={{
                    startAdornment: <Search sx={{ mr: 1, color: 'text.secondary' }} />
                  }}
                  sx={{ mr: 2, width: 200 }}
                />
                <Button variant="outlined" startIcon={<Add />}>
                  Add to Watchlist
                </Button>
              </Box>
            </Box>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Symbol</TableCell>
                    <TableCell>Name</TableCell>
                    <TableCell align="right">Price</TableCell>
                    <TableCell align="right">Change</TableCell>
                    <TableCell align="right">Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {watchlist.map((item) => (
                    <TableRow key={item.symbol}>
                      <TableCell>
                        <Typography variant="subtitle2" fontWeight="bold">
                          {item.symbol}
                        </Typography>
                      </TableCell>
                      <TableCell>{item.name}</TableCell>
                      <TableCell align="right">{formatCurrency(item.price)}</TableCell>
                      <TableCell align="right">
                        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
                          {getChangeIcon(item.change)}
                          <Typography 
                            variant="body2" 
                            color={getChangeColor(item.change)}
                            sx={{ ml: 0.5 }}
                          >
                            {formatPercent(item.changePercent)}
                          </Typography>
                        </Box>
                      </TableCell>
                      <TableCell align="right">
                        <Button size="small" variant="outlined" sx={{ mr: 1 }}>
                          Buy
                        </Button>
                        <IconButton size="small">
                          <Settings />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );

  const renderTradingTab = () => (
    <Grid container spacing={3}>
      <Grid item xs={12} md={6}>
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Place Order
            </Typography>
            <Box component="form" sx={{ mt: 2 }}>
              <FormControl fullWidth margin="normal">
                <InputLabel>Order Type</InputLabel>
                <Select defaultValue="market">
                  <MenuItem value="market">Market Order</MenuItem>
                  <MenuItem value="limit">Limit Order</MenuItem>
                  <MenuItem value="stop">Stop Order</MenuItem>
                </Select>
              </FormControl>
              
              <FormControl fullWidth margin="normal">
                <InputLabel>Action</InputLabel>
                <Select defaultValue="buy">
                  <MenuItem value="buy">Buy</MenuItem>
                  <MenuItem value="sell">Sell</MenuItem>
                </Select>
              </FormControl>
              
              <TextField
                fullWidth
                margin="normal"
                label="Symbol"
                placeholder="e.g., AAPL"
              />
              
              <TextField
                fullWidth
                margin="normal"
                label="Quantity"
                type="number"
              />
              
              <TextField
                fullWidth
                margin="normal"
                label="Price (for limit orders)"
                type="number"
              />
              
              <Button
                fullWidth
                variant="contained"
                size="large"
                sx={{ mt: 2 }}
              >
                Place Order
              </Button>
            </Box>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} md={6}>
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Recent Trades
            </Typography>
            <TableContainer>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell>Date</TableCell>
                    <TableCell>Type</TableCell>
                    <TableCell>Symbol</TableCell>
                    <TableCell align="right">Shares</TableCell>
                    <TableCell align="right">Price</TableCell>
                    <TableCell align="right">Total</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {recentTrades.map((trade, index) => (
                    <TableRow key={index}>
                      <TableCell>{trade.date}</TableCell>
                      <TableCell>
                        <Chip 
                          label={trade.type} 
                          color={trade.type === 'BUY' ? 'success' : 'error'}
                          size="small"
                        />
                      </TableCell>
                      <TableCell>{trade.symbol}</TableCell>
                      <TableCell align="right">{trade.shares}</TableCell>
                      <TableCell align="right">{formatCurrency(trade.price)}</TableCell>
                      <TableCell align="right">{formatCurrency(trade.total)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );

  const renderAnalyticsTab = () => (
    <Grid container spacing={3}>
      <Grid item xs={12} md={6}>
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Portfolio Performance
            </Typography>
            <Box sx={{ height: 300, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Typography color="text.secondary">
                Chart component would go here
              </Typography>
            </Box>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} md={6}>
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Asset Allocation
            </Typography>
            <Box sx={{ height: 300, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Typography color="text.secondary">
                Pie chart component would go here
              </Typography>
            </Box>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12}>
        <Card>
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Risk Analysis
            </Typography>
            <Grid container spacing={2}>
              <Grid item xs={12} md={4}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h4" color="primary">7.2</Typography>
                  <Typography variant="body2" color="text.secondary">Beta</Typography>
                </Box>
              </Grid>
              <Grid item xs={12} md={4}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h4" color="warning.main">15.8%</Typography>
                  <Typography variant="body2" color="text.secondary">Volatility</Typography>
                </Box>
              </Grid>
              <Grid item xs={12} md={4}>
                <Box sx={{ textAlign: 'center' }}>
                  <Typography variant="h4" color="success.main">1.24</Typography>
                  <Typography variant="body2" color="text.secondary">Sharpe Ratio</Typography>
                </Box>
              </Grid>
            </Grid>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );

  return (
    <Container maxWidth="xl" sx={{ mt: 2, mb: 4 }}>
      {/* Header */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Box>
          <Typography variant="h4" component="h1" gutterBottom>
            TradeShift Portfolio
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            Welcome back, {user?.fullName}
          </Typography>
        </Box>
        
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Chip 
            label={user?.role} 
            color={user?.role === 'ADMIN' ? 'secondary' : 'primary'}
            icon={<Person />}
          />
          <Button
            variant="outlined"
            startIcon={<Settings />}
            onClick={() => setShowProfileDialog(true)}
          >
            Profile
          </Button>
          <Button
            variant="outlined"
            color="error"
            startIcon={<Logout />}
            onClick={handleLogout}
          >
            Logout
          </Button>
        </Box>
      </Box>

      {message && (
        <Alert severity="success" sx={{ mb: 3 }} onClose={() => setMessage('')}>
          {message}
        </Alert>
      )}

      {error && (
        <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError('')}>
          {error}
        </Alert>
      )}

      {/* Navigation Tabs */}
      <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 3 }}>
        <Tabs value={activeTab} onChange={handleTabChange}>
          <Tab label="Overview" icon={<Assessment />} />
          <Tab label="Watchlist" icon={<Notifications />} />
          <Tab label="Trading" icon={<ShowChart />} />
          <Tab label="Analytics" icon={<Timeline />} />
        </Tabs>
      </Box>

      {/* Tab Content */}
      {activeTab === 0 && renderOverviewTab()}
      {activeTab === 1 && renderWatchlistTab()}
      {activeTab === 2 && renderTradingTab()}
      {activeTab === 3 && renderAnalyticsTab()}

      {/* Profile Dialog */}
      <Dialog open={showProfileDialog} onClose={() => setShowProfileDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Profile Settings</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
            <Avatar sx={{ bgcolor: 'primary.main', mr: 2, width: 56, height: 56 }}>
              <Person />
            </Avatar>
            <Box>
              <Typography variant="h6">{user?.fullName}</Typography>
              <Typography variant="body2" color="text.secondary">{user?.email}</Typography>
              <Chip 
                label={user?.role} 
                color={user?.role === 'ADMIN' ? 'secondary' : 'primary'}
                size="small"
                sx={{ mt: 1 }}
              />
            </Box>
          </Box>
          
          <Divider sx={{ mb: 2 }} />
          
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
            <Email sx={{ mr: 1, color: 'text.secondary' }} />
            <Typography variant="body2">{user?.email}</Typography>
          </Box>
          
          {user?.mobile && (
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Phone sx={{ mr: 1, color: 'text.secondary' }} />
              <Typography variant="body2">{user?.mobile}</Typography>
            </Box>
          )}
          
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
            <Security sx={{ mr: 1, color: 'text.secondary' }} />
            <Typography variant="body2">
              2FA: {user?.twoFactorAuth?.isEnabled ? 'Enabled' : 'Disabled'}
            </Typography>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setShowProfileDialog(false)}>Close</Button>
          <Button variant="contained">Edit Profile</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default PortfolioDashboard;
