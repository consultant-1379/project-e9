// Fetch the JSON data
        // JavaScript function to toggle the visibility of the checkboxes
function toggleDropdown() {
    var checkboxes = document.getElementById("checkboxes");
    if (checkboxes.style.display === "block") {
        checkboxes.style.display = "none";
    } else {
        checkboxes.style.display = "block";
    }
}
function generateCards(){

fetch(`new.json?_=${new Date().getTime()}`)
  .then(response => response.json())
  .then(data => {

    const cardContainer = document.getElementById('card-container');
    const searchInput = document.getElementById('search-input');
    const searchTitleCheckbox = document.getElementById('search-title');
    const searchContentCheckbox = document.getElementById('search-content');
    const searchLeadCheckbox = document.getElementById('search-leadeng');
    const searchEmailCheckbox = document.getElementById('search-leadeng-email');
    const searchDescriptionCheckbox = document.getElementById('search-description');
    const searchDateCheckbox = document.getElementById('search-date');
    const searchVersionCheckbox = document.getElementById('search-version');

    // Add more checkboxes for other properties if needed

// Function to filter cards based on search input and checkbox selections
function filterCards(query) {
    cardContainer.innerHTML = ''; // Clear the container

    // Create an object to store unique titles and their details
    const uniqueTitles = {};

    // Filter and group the data by title
    data.forEach(item => {
      const titleMatch = searchTitleCheckbox.checked && item.title.toLowerCase().includes(query);
      const contentMatch = searchContentCheckbox.checked && item.category.toLowerCase().includes(query);
      const leadMatch = searchLeadCheckbox.checked && item.leadengname.toLowerCase().includes(query);
      const emailMatch = searchEmailCheckbox.checked && item.leadengemail.toLowerCase().includes(query);
      const descriptionMatch = searchDescriptionCheckbox.checked && item.description.toLowerCase().includes(query);
      const dateMatch = searchDateCheckbox.checked && item.date.toLowerCase().includes(query);
      const versionMatch = searchVersionCheckbox.checked && item.version.toLowerCase().includes(query);

      // Check if any checkbox is checked and at least one field matches the query
      if ((searchTitleCheckbox.checked || searchContentCheckbox.checked || searchLeadCheckbox.checked || searchEmailCheckbox.checked || searchDescriptionCheckbox.checked || searchDateCheckbox.checked || searchVersionCheckbox.checked) &&
          (titleMatch || contentMatch || leadMatch || emailMatch || descriptionMatch || dateMatch || versionMatch)) {
        if (!uniqueTitles[item.title]) {
          uniqueTitles[item.title] = {
            title: item.title,
            details: [],
            versions: [], // Store versions for each title
          };
        }
        uniqueTitles[item.title].details.push(item);
        uniqueTitles[item.title].versions.push(item.version);
      }
    });

// Inside the loop where you create cards for each unique title
for (const title in uniqueTitles) {
  const card = document.createElement('div');
  card.classList.add('card'); // You can apply CSS styles to this class in your stylesheet

  const cardTitle = document.createElement('h2');
  cardTitle.textContent = uniqueTitles[title].title;

  // Create a dropdown menu for "version" values
  const versionDropdown = document.createElement('select');
  versionDropdown.classList.add('version-dropdown');

  // Create a default option
  const defaultOption = document.createElement('option');
  defaultOption.value = ''; // Set the value to an empty string or any default value you prefer
  defaultOption.textContent = 'Select a Version'; // Set the default display text
  versionDropdown.appendChild(defaultOption);

  // Add versions to the dropdown
  uniqueTitles[title].versions.forEach(version => {
      const versionOption = document.createElement('option');
      versionOption.value = version;
      versionOption.textContent = version;
      versionDropdown.appendChild(versionOption);
  });

  // Add an event listener to the dropdown to display details and update the "Open API" link
  versionDropdown.addEventListener('change', () => {
      const selectedVersion = versionDropdown.value;
      const selectedDetails = uniqueTitles[title].details.find(item => item.version === selectedVersion);
      displayDetails(selectedDetails, card);

      // Update the "Open API" link's href based on the selected version
      openApiLink.href = `/api/${title}:v${selectedVersion}/openapi.yaml`;

      openApiLink.style.display = 'inline-block';
  });

  // Set the default option as selected
  versionDropdown.value = ''; // Set the value to match the default option

  const detailsContainer = document.createElement('div');
  detailsContainer.classList.add('details-container');

  // Create the initial "Open API" link with a placeholder href
  const openApiLink = document.createElement('a');
  openApiLink.href = ''; // Placeholder href, will be updated when a version is selected
  openApiLink.classList.add('btn', 'btn-primary');
  openApiLink.textContent = 'Open API';
  openApiLink.style.display="none";

  card.appendChild(cardTitle);
  card.appendChild(versionDropdown);
  card.appendChild(detailsContainer);
  card.appendChild(openApiLink); // Add the Open API link
  cardContainer.appendChild(card);
  alert(cardContainer);
}


  }


    // Function to display details
    function displayDetails(details, card) {
      const detailsContainer = card.querySelector('.details-container');
      if (detailsContainer) {
        detailsContainer.innerHTML = ''; // Clear previous details

        // Display the selected object's details
        for (const key in details) {
          if (details.hasOwnProperty(key)) {
            const detail = document.createElement('p');
            detail.textContent = `${key}: ${details[key]}`;
            detailsContainer.appendChild(detail);
          }
        }
      }
    }

    // Event listener for the search input
    searchInput.addEventListener('input', () => {
        const query = searchInput.value.toLowerCase();
        filterCards(query);
      });

      // Event listener for the checkboxes
      searchTitleCheckbox.addEventListener('change', () => {
        const query = searchInput.value.toLowerCase();
        filterCards(query);
      });

      searchContentCheckbox.addEventListener('change', () => {
        const query = searchInput.value.toLowerCase();
        filterCards(query);
      });

      searchLeadCheckbox.addEventListener('change', () => {
          const query = searchInput.value.toLowerCase();
          filterCards(query);
      });

      searchEmailCheckbox.addEventListener('change', () => {
        const query = searchInput.value.toLowerCase();
        filterCards(query);
    });

    searchDescriptionCheckbox.addEventListener('change', () => {
      const query = searchInput.value.toLowerCase();
      filterCards(query);
  });

  searchDateCheckbox.addEventListener('change', () => {
    const query = searchInput.value.toLowerCase();
    filterCards(query);
  });

  searchVersionCheckbox.addEventListener('change', () => {
    const query = searchInput.value.toLowerCase();
    filterCards(query);
  });

    // ... (your previous code)

    // Initialize the cards with all data
    filterCards('');
    })
    .catch(error => console.error(error));

        // set the dimensions and margins of the graph
        var margin = {top: 10, right: 30, bottom: 30, left: 40},
            width = 1200 - margin.left - margin.right, // Increase width
            height = 1000 - margin.top - margin.bottom; // Increase height

        // append the svg object to the body of the page
        var svg = d3.select("#my_graph")
            .append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        // Define the links array in a more accessible scope
        var links = [];

        d3.json("network.json", function(data) {
            // Initialize the links
            links = data.links;

            // Calculate initial positions of nodes in a circular arrangement
            var radius = Math.min(width, height) / 2 - 50; // Adjust the radius as needed
            var centerX = width / 3;
            var centerY = height / 3;

            data.nodes.forEach(function(node, index) {
                var angle = (index / data.nodes.length) * 2 * Math.PI;
                var degree = data.nodes.length; // Get the degree of the node
                var adjustedRadius = radius + degree * 10; // Adjust spacing based on degree
                node.x = centerX + adjustedRadius * Math.cos(angle);
                node.y = centerY + adjustedRadius * Math.sin(angle);
            });

            // Initialize the nodes
            var link = svg
                .selectAll("line")
                .data(data.links)
                .enter()
                .append("line")
                .style("stroke", "#aaa")
                .attr("class", function(d) {
                    return "link-" + d.id; // Assign a unique class based on the "id" property
                });
            var node = svg
                .selectAll("circle")
                .data(data.nodes)
                .enter()
                .append("g")
                .attr("class", "node");

            node.append("circle")
                .attr("r", 20)
                .style("fill", "#69b3a2")
                .attr("class", function(d) {
                    return "circle-" + d.id;
                });

            node.append("text")
                .attr("dx", 0)
                .attr("dy", -25)
                .style("text-anchor", "middle")
                .text(function(d) {
                    return "service " + d.id;
                });

            var simulation = d3.forceSimulation(data.nodes).force("link", d3.forceLink()
                    .id(function(d) { return d.id; })
                    .links(data.links)
                    .distance(100)) // Increase the distance value
                .force("charge", d3.forceManyBody().strength(-800)) // Increase the strength value
                .force("center", d3.forceCenter(width / 2, height / 2))
                .on("end", ticked);

            console.log(data.links)
            // This function is run at each iteration of the force algorithm, updating the nodes' position.
            function ticked() {
                link
                    .attr("x1", function(d) { return d.source.x; })
                    .attr("y1", function(d) { return d.source.y; })
                    .attr("x2", function(d) { return d.target.x; })
                    .attr("y2", function(d) { return d.target.y; });

                node
                    .attr("transform", function(d) {
                        return "translate(" + d.x + "," + d.y + ")";
                    });
            }

            // Function to remove all links
            function removeAllLinks() {
                // Clear the links data
                links = [];

                // Remove the existing links from the DOM
                svg.selectAll("line").remove();

                // Restart the simulation to reflect the changes
                simulation.alpha(1).restart();
            }

            // Function to add a new link
            function addNewLink() {
                // Define the source and target nodes for the new link
                var sourceNodeIndex = 0; // Replace with the desired source node index
                var targetNodeIndex = 2; // Replace with the desired target node index

                // Create a new link data entry
                var newLink = {
                    source: data.nodes[sourceNodeIndex],
                    target: data.nodes[targetNodeIndex]
                };

                // Check if the link already exists to avoid duplication
                var linkExists = links.some(function(link) {
                    return (link.source === newLink.source && link.target === newLink.target) ||
                        (link.source === newLink.target && link.target === newLink.source);
                });

                if (!linkExists) {
                    // Add the new link data entry to the links array
                    links.push(newLink);

                    // Redraw or update the visualization as needed
                    // For example, you can add the new link to the DOM as a line
                    var newLinkElement = svg.selectAll("line")
                        .data(links)
                        .enter()
                        .append("line")
                        .style("stroke", "#FF0000");

                    // Update the links in the force simulation
                    simulation.force("link", d3.forceLink()
                    .id(function(d) { return d.id; })
                    .links(data.links)
                    .distance(100)) // Increase the distance value

                    simulation.force("charge", d3.forceManyBody().strength(-800)) // Increase the strength value

                    simulation.on("end", ticked);

                    // Restart the simulation to reflect the changes
                    simulation.alpha(1).restart();
                }
            }

        });
}
    const id = "123";
    fetch(`http://localhost:8080/api/get/${id}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json(); // Parse the response as JSON
      })
      .then(data => {
        // Handle the JSON data
        console.log(data.message); // Access the "message" property in the JSON
      })
      .catch(error => {
        // Handle errors
        console.error('There was a problem with the fetch operation:', error);
      });

    const uploadButton = document.getElementById('upload-button');
    const openapiFileInput = document.getElementById('openapi-file');

    uploadButton.addEventListener('click', async () => {
      const file = openapiFileInput.files[0];
      alert(file)
      if (file) {
            const formData = new FormData();
            formData.append('file', file);

        fetch(`http://localhost:8080/api/upload`, {
          method: 'POST',
          body: formData,
        })
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            alert(response);
            return response.json(); // Parse the response as JSON
          })
    fetch(`http://localhost:8080/api/new`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        generateCards()
        return response.json(); // Parse the response as JSON
      })
      .then(data => {
        // Handle the JSON data
        console.log(data.message); // Access the "message" property in the JSON
      })
      .catch(error => {
        // Handle errors
        console.error('There was a problem with the fetch operation:', error);
      });

// Close the 'generateCards' function
}}); // Close the 'fetch('cards.json')' promise


