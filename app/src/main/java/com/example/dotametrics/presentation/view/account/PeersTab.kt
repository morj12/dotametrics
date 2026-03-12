package com.example.dotametrics.presentation.view.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dotametrics.R
import com.example.dotametrics.domain.entity.remote.players.peers.PeersResult
import com.example.dotametrics.util.Datetime

@Composable
fun PeersScreen(
    viewModel: AccountViewModel,
    onPeerClick: (Long) -> Unit
) {
    val peers by viewModel.peers.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        if (peers.isEmpty()) viewModel.loadPeers()
    }

    if (peers.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp, start = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = peers,
                key = { it.accountId ?: 0L }
            ) { peer ->
                PeerItem(peer, onPeerClick)
            }
        }
    }
}

@Composable
fun PeerItem(peer: PeersResult, onClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(peer.accountId ?: 0L) },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = peer.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_person)
            )

            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(
                    text = peer.name ?: "Unknown",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "Games: ${peer.withGames}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                if (peer.lastPlayed != null) {
                    Text(
                        text = "Last: ${Datetime.formatDate(peer.lastPlayed!!)}",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                val winrate = if (peer.withGames != null && peer.withGames!! > 0) {
                    peer.withWin!!.toDouble() / peer.withGames!! * 100
                } else 0.0
                Text(
                    text = stringResource(id = R.string.percent_format, winrate),
                    color = if (winrate >= 50) colorResource(id = R.color.green) else colorResource(id = R.color.red),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${peer.withWin}W - ${(peer.withGames ?: 0) - (peer.withWin ?: 0)}L",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }
    }
}
